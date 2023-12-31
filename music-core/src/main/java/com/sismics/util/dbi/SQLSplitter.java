package com.sismics.util.dbi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SQLSplitter implements Iterable<CharSequence> {

    /**
     * Skips the index past the quote.
     *
     * @param s     The string
     * @param start The starting character of the quote.
     * @return The index that skips past the quote starting at start. If the quote does not start at that point, it simply returns start.
     */
    static int consumeQuote(final CharSequence s, final int start) {
        if (start >= s.length()) {
            return start;
        }
    
        char ender = getQuoteEnder(s.charAt(start));
        if (ender == '\0') {
            return start;
        }
    
        boolean escaped = false;
        for (int i = start + 1; i < s.length(); ++i) {
            if (escaped) {
                escaped = false;
                continue;
            }
    
            char c = s.charAt(i);
            if (c == '\\') {
                escaped = true;
            } else if (c == ender) {
                return i + 1;
            }
        }
    
        return s.length();
    }
    
    private static char getQuoteEnder(char c) {
        switch (c) {
            case '\'':
            case '"':
            case '[':
            case '`':
                return c;
    
            case '$':
                return getDollarQuoteEnder();
    
            default:
                return '\0';
        }
    }
    
    private static char getDollarQuoteEnder() {
        return '$';
    }
    
    private static int consumeDollarQuote(final CharSequence s, final int start) {
        int quoteEnd = start + 1;
        for (; s.charAt(quoteEnd) != '$'; ++quoteEnd) {
            if (quoteEnd >= s.length()) {
                return quoteEnd;
            }
        }
    
        int i = quoteEnd + 1;
        while (i < s.length()) {
            if (s.charAt(i) == '$') {
                boolean match = true;
                for (int j = start; j <= quoteEnd && i < s.length(); ++j, ++i) {
                    if (s.charAt(i) != s.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return i;
                }
            } else {
                ++i;
            }
        }
    
        return i;
    }
    

    private static boolean isNewLine(char c) {
        return c == '\n' || c == '\r';
    }

    /**
     * Returns the index of the next line from a start location.
     */
    private static int consumeTillNextLine(final CharSequence s, int start) {
        while (start < s.length() && !isNewLine(s.charAt(start)))
            ++start;
        while (start < s.length() && isNewLine(s.charAt(start)))
            ++start;
        return start;
    }

    private static boolean isNext(final CharSequence s, final int start, final char c) {
        if (start + 1 < s.length())
            return s.charAt(start + 1) == c;
        return false;
    }

    /**
     * Skips the index past the comment.
     *
     * @param s     The string
     * @param start The starting character of the comment
     * @return The index that skips past the comment starting at start. If the comment does not start at that point, it simply returns start.
     */
    private static int consumeComment(final CharSequence s, int start) {
        if (start >= s.length()) {
            return start;
        }
    
        char firstChar = s.charAt(start);
        switch (firstChar) {
            case '-':
                return consumeSingleLineComment(s, start);
    
            case '#':
                return consumeSingleLineComment(s, start);
    
            case '/':
                return consumeMultiLineComment(s, start);
    
            case '{':
                return consumeBlock(s, start, '{', '}');
    
            default:
                return start;
        }
    }
    
    private static int consumeSingleLineComment(CharSequence s, int start) {
        return consumeTillNextLine(s, start);
    }
    
    private static int consumeMultiLineComment(CharSequence s, int start) {
        if (isNext(s, start, '*')) {
            start += 2;
            while (start < s.length()) {
                if (s.charAt(start) == '*') {
                    ++start;
                    if (start < s.length() && s.charAt(start) == '/') {
                        return start + 1;
                    }
                } else {
                    ++start;
                }
            }
        }
        return start;
    }
    
    private static int consumeBlock(CharSequence s, int start, char open, char close) {
        while (start < s.length() && s.charAt(start) != close) {
            ++start;
        }
        return start + 1;
    }
    

    private static int consumeParentheses(final CharSequence s, int start) {
        if (start >= s.length()) return start;
        switch (s.charAt(start)) {
            case '(':
                ++start;
                while (start < s.length()) {
                    if (s.charAt(start) == ')')
                        return start + 1;
                    start = nextChar(s, start);
                }
                break;
            default:
                break;
        }
        return start;
    }

    private static int nextChar(final CharSequence sql, final int start) {
        int i = consumeParentheses(sql, consumeComment(sql, consumeQuote(sql, start)));
        if (i == start) return Math.min(start + 1, sql.length());
        do {
            final int j = consumeParentheses(sql, consumeComment(sql, consumeQuote(sql, i)));
            if (j == i)
                return i;
            i = j;
        } while (true);
    }

    /**
     * Splits the SQL "properly" based on semicolons. Respecting quotes and comments.
     */
    public static ArrayList<String> splitSQL(final CharSequence sql) {
        final ArrayList<String> ret = new ArrayList<>();
        for (CharSequence c : new SQLSplitter(sql)) {
            ret.add(c.toString());
        }
        return ret;
    }

    private final CharSequence sql;

    private SQLSplitter(final CharSequence sql) {
        this.sql = sql;
    }

    @Override
    public Iterator<CharSequence> iterator() {
        return new Iterator<CharSequence>() {
            int i = 0, prev = 0;

            @Override
            public boolean hasNext() {
                return prev < sql.length();
            }

            @Override
            public CharSequence next() {
                while (i < sql.length()) {
                    if (sql.charAt(i) == ';') {
                        ++i;
                        // check "double semicolon" -> used to escape a semicolon and avoid splitting
                        if ((i < sql.length() && sql.charAt(i) == ';')) {
                            ++i;
                        } else {
                            CharSequence ret = sql.subSequence(prev, i).toString().replace(";;", ";");
                            prev = i;
                            return ret;
                        }
                    }
                    i = nextChar(sql, i);
                }
                if (prev != i) {
                    CharSequence ret = sql.subSequence(prev, i).toString().replace(";;", ";");
                    prev = i;
                    return ret;
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
