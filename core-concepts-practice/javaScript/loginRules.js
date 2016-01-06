/**
 * 
 */
function checkForm(form) {
	if (form.username.value == "") {
		alert("Error: Username cannot be blank!");
		form.username.focus();
		return false;
	}
	if (form.pwd1.value != "" && form.pwd1.value == form.pwd2.value) {
		if (form.pwd1.value.length < 6) {
			alert("Error: Password must contain at least six characters!");
			form.pwd1.focus();
			return false;
		}
		if (form.pwd1.value == form.username.value) {
			alert("Error: Password must be different from Username!");
			form.pwd1.focus();
			return false;
		}
	} else {
		alert("Error: Please check that you've entered and confirmed your password!");
		form.pwd1.focus();
		return false;
	}

	alert("You entered a valid password: " + form.pwd1.value);
	return true;
}

function checkLogin(form) {
	if (form.username.value == "") {
		alert("Error: Username cannot be blank!");
		form.username.focus();
		return false;
	}
	if (form.pwd1.value == "") {
		alert("Error: Please Enter Password");
		form.pwd1.focus();
		return false;
	}
	if (form.pwd1.value.length < 6) {
		alert("Error: Password Should be Greater Than 6 chars");
		form.pwd1.focus();
		return false;
	}
	return true;
}