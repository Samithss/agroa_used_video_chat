function validateform(){
var email=document.loginForm.email.value;
var password=document.loginForm.password.value;

if (email==null || email==""||email.length >= 35){
  alert("ENTER VALID EMAIL ID");
  return false;
}
else if(password==null || password==""||password.length >= 20){
  alert("ENTER VALID PASSWORD");
  return false;
  }
  else
  return true;
}
