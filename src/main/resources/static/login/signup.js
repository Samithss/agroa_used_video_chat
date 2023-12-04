

function validform()
{
    var firstName=document.getElementById("firstName").value;
//    console.log(firstName);
     var lastName=document.getElementById("lastName").value;
//      console.log(lastName);
    var contact=document.getElementById("contactNo").value;
//     console.log(contact);
    var password=document.getElementById("password").value;
//     console.log(password);
     var email=document.getElementById("email").value;
//      console.log(email);

    if (email==null || email.trim()==""||email.length >= 35){
        document.getElementById("error").innerHTML="Enter Email-Id" ;
          return false;
      }else if(firstName==null || firstName.trim()==""||firstName.length >= 20){
            document.getElementById("error").innerHTML="Enter valid First Name";
          return false;
      }
      else if(contact==null || contact.trim()=="" || contact.length!=10)
      {
       document.getElementById("error").innerHTML="Enter Valid Phone number";
        return false;
      }
      else if(password==null || password.trim()==""||password.length >= 20){
         document.getElementById("error").innerHTML="Enter valid Password";
        return false;
      }
      else
      return true;

}