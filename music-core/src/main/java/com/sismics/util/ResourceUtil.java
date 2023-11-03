package com.sismics.util;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Resource utilities.
 *
 * @author jtremeaux 
 */
public class ResourceUtil {

    /**
     * List files inside a directory. The path can be a directory on the filesystem, or inside a JAR.
     *
     * @param clazz Class
     * @param path Path
     * @param filter Filter
     * @return List of files
     */
    public static List<String> list(Class<?> clazz, String path, FilenameFilter filter) throws URISyntaxException, IOException {
        URL dirUrl = clazz.getResource(path);

        if (dirUrl == null) {
            String className = clazz.getName().replace(".", "/") + ".class";
            dirUrl = clazz.getClassLoader().getResource(className);
        }

        if (dirUrl.getProtocol().equals("jar")) {
            return listFilesInJar(dirUrl, path, filter);
        } else if (dirUrl.getProtocol().equals("file")) {
            return listFilesInDirectory(dirUrl, filter);
        } else {
            throw new UnsupportedOperationException(MessageFormat.format("Cannot list files for URL {0}", dirUrl));
        }
    }

    private static List<String> listFilesInJar(URL jarUrl, String path, FilenameFilter filter) throws IOException {
        String jarPath = jarUrl.getPath().substring(5, jarUrl.getPath().indexOf("!"));
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));

        try {
            Enumeration<JarEntry> entries = jar.entries();
            Set<String> fileSet = new HashSet<>();

            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement().getName();

                if (!entryName.startsWith(path)) {
                    continue;
                }

                String name = entryName.substring(path.length());
                int checkSubdir = name.indexOf("/");

                if (checkSubdir >= 0) {
                    name = name.substring(0, checkSubdir);
                }

                if (!name.isEmpty() && (filter == null || filter.accept(null, name))) {
                    fileSet.add(name);
                }
            }

            return new ArrayList<>(fileSet);
        } finally {
            jar.close();
        }
    }

    private static List<String> listFilesInDirectory(URL dirUrl, FilenameFilter filter) {
        File dir = new File(dirUrl.getPath());
        String[] files = dir.list(filter);
        return Arrays.asList(files);
    }

    /**
     * List files inside a directory. The path can be a directory on the filesystem, or inside a JAR.
     *
     * @param clazz Class
     * @param path Path
     * @return List of files
     */
    public static List<String> list(Class<?> clazz, String path) throws URISyntaxException, IOException {
        return list(clazz, path, null);
    }
}