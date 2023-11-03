<div class="row">
    <div class="col-sm-offset-3 col-sm-6">
    <h2 align="center">Register</h1>
  
    <form novalidate name="editUserForm" ng-click="editUserForm.submitted = true">
      <div class="form-group" ng-class="{ 'has-error': !editUserForm.username.$valid && editUserForm.submitted, success: editUserForm.username.$valid }">
        <label for="inputUsername">Username</label>
    
        <div class="form-group">
          <input name="username" type="text" id="inputUsername" required ng-disabled="isEdit()" class="form-control"
                 ng-minlength="3" ng-maxlength="50" placeholder="Username" ng-model="user.username"/>
        </div>
  
        <div ng-show="editUserForm.submitted">
          <span class="help-block" ng-show="editUserForm.username.$error.required">Required</span>
          <span class="help-block" ng-show="editUserForm.username.$error.minlength">Too short</span>
          <span class="help-block" ng-show="editUserForm.username.$error.maxlength">Too long</span>
        </div>
      </div>
  
      <div class="form-group" ng-class="{ 'has-error': !editUserForm.email.$valid && editUserForm.submitted, success: editUserForm.email.$valid }">
        <label for="inputEmail">E-mail</label>
    
        <div class="form-group">
          <input name="email" type="email" id="inputEmail" required class="form-control"
                 ng-minlength="3" ng-maxlength="50" placeholder="E-mail" ng-model="user.email"/>
        </div>
  
        <div ng-show="editUserForm.submitted">
          <span class="help-block" ng-show="editUserForm.email.$error.required">Required</span>
          <span class="help-block" ng-show="editUserForm.email.$error.email">Must be a valid e-mail</span>
          <span class="help-block" ng-show="editUserForm.email.$error.minlength">Too short</span>
          <span class="help-block" ng-show="editUserForm.email.$error.maxlength">Too long</span>
        </div>
      </div>
  
      <div class="form-group" ng-class="{ 'has-error': !editUserForm.password.$valid && editUserForm.submitted, success: editUserForm.password.$valid }">
        <label for="inputPassword">Password</label>
    
        <div class="form-group">
          <input name="password" type="password" id="inputPassword" ng-required="!isEdit()" class="form-control"
                 ng-minlength="8" ng-maxlength="50" placeholder="Password" ng-model="user.password"/>
        </div>
  
        <div ng-show="editUserForm.submitted">
          <span class="help-block" ng-show="editUserForm.password.$error.required">Required</span>
          <span class="help-block" ng-show="editUserForm.password.$error.minlength">Too short</span>
          <span class="help-block" ng-show="editUserForm.password.$error.maxlength">Too long</span>
        </div>
      </div>
  
      <div class="form-group" ng-class="{ 'has-error': !editUserForm.passwordconfirm.$valid && editUserForm.submitted, success: editUserForm.passwordconfirm.$valid }">
        <label for="inputPasswordConfirm">Password (confirm)</label>
    
        <div class="form-group">
          <input name="passwordconfirm" type="password" id="inputPasswordConfirm" class="form-control"
                 ui-validate="'$value == user.password'" ui-validate-watch="'user.password'"
                 placeholder="Password (confirm)" ng-model="user.passwordconfirm"/>
        </div>
  
        <div ng-show="editUserForm.submitted">
          <span class="help-block" ng-show="editUserForm.passwordconfirm.$error.validator">Password and password confirmation must match</span>
        </div>
      </div>
  
      <button type="submit" class="btn btn-primary btn-block" ng-click="register()">
        <span class="glyphicon glyphicon-ok"></span> Register
      </button>
  
    </form>
  
    <br/><br/>
  
    <div class="form-group">
      <p class="text-center text-danger">
        <span class="glyphicon glyphicon-warning-sign"></span>
        Already a user?
      </p>
      <button type="submit" class="btn btn-primary btn-block" ng-click="login()">
        <span class="glyphicon glyphicon-ok"></span> Login 
      </button>
      </div>
    </div>
  </div>
  