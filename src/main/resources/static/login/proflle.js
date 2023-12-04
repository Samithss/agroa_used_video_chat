UserDetails();
function UserDetails(){
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
    const obj = JSON.parse(this.responseText)
            const emailElement = document.getElementById("email");
            const firstNameElement = document.getElementById("firstName");
            const lastNameElement = document.getElementById("lastName");
            const contactNoElement = document.getElementById("contactNo");

            emailElement.textContent = obj.email;
            firstNameElement.textContent = obj.firstName;
            lastNameElement.textContent = obj.lastName;
            contactNoElement.textContent = obj.contactNo;

}
xhttp.open("GET", "/activeDetails/fetch");
xhttp.send();
}

function logoutSession(){
 const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
    if (xhttp.status === 200) {
    window.location.href = "/login";
      }
    }
    xhttp.open("GET", "/logout");
    xhttp.send();

}
 document.getElementById('buttonload').style.visibility = 'hidden';
 var pageNumber = 1;
 function searchVideoCAllHistory(pageNumber)
 {
  document.getElementById('buttonload').style.visibility = 'visible';
    $("#grid").jqGrid("GridUnload");
    let date  = document.getElementById("videoCallDate").value;
    console.log(date);
if (date !== "") {
    let userid = document.getElementById("searchByEmail").value;
    const xhttp=new XMLHttpRequest;
    xhttp.onload=function()
   {
         if (this.readyState === 4 && this.status === 200) {
              console.log(this.responseText);
              const responseData = JSON.parse(this.responseText);
                          console.log(responseData);
                           document.getElementById('buttonload').style.visibility = 'hidden';
                          initializeGrid(responseData);
                }
          }
     const data = {
         currentDateE: date,
         currentUserId:userid,
         pageNo:pageNumber
     };
     xhttp.open("POST", "/searchVideoCAllHistory", true);
     xhttp.setRequestHeader("Content-type", "application/json");
     xhttp.send(JSON.stringify(data));
    }
    else
    alert("PLEASE SELECT DATE");
    document.getElementById('buttonload').style.visibility = 'hidden';
}



function initializeGrid(data) {
    $("#grid").jqGrid("clearGridData");
    $("#grid").jqGrid({
        datatype: "local",
        colNames: ["Room ID", "User ID", "Joined Time", "Left Time", "Duration"],
        colModel: [
            { name: "roomCode", width: 220 },
            { name: "userId", width: 220 },
            { name: "userJoinedTime", width: 220 },
            { name: "userLeftTime", width: 220 },
            { name: "duration", width: 220 }
        ],
        data: data,
    });
}

function clearGrid() {
    $("#grid").jqGrid("clearGridData");
}

function totalNumberOfRecords(){
pageNumber=1;
let date  = document.getElementById("videoCallDate").value;
let userid = document.getElementById("searchByEmail").value;
   const xhttp=new XMLHttpRequest;
    xhttp.onload=function()
   {
    if (this.readyState === 4 && this.status === 200) {
                 console.log(this.responseText);
                     totalRecords = JSON.parse(this.responseText);
                       if (totalRecords > 10){
                         document.querySelector('.pagination').style.display = "block";
                       console.log("total" + totalRecords);
                       generatePaginationLinks(pageNumber);
                       }else{
                       document.getElementById('paginationList').innerHTML="";
                        searchVideoCAllHistory(pageNumber)
                       }

   }
   }
     const data = {
            currentDate: date,
            userId:userid
        };
          xhttp.open("POST", "/totalNumberOfRecords", true);
             xhttp.setRequestHeader("Content-type", "application/json");
             xhttp.send(JSON.stringify(data));
 }

var pageSize = 10;
const paginationList = document.getElementById('paginationList');

function generatePaginationLinks(currentPage, event) {
  console.log(event);
  console.log(currentPage)
  pageNumber = currentPage;
  searchVideoCAllHistory(pageNumber)
  console.log("total insideFn" + totalRecords);
  const totalPages = Math.ceil(totalRecords / pageSize);
  paginationList.innerHTML = '';

  if (currentPage > 1) {
    addPaginationLink('< Previous', currentPage - 1,currentPage);
  }
  addPaginationLink(1, 1,currentPage);

  if (currentPage > 3) {
    addEllipsis();
  }

  for (let page = Math.max(2, currentPage - 1); page <= Math.min(currentPage + 1, totalPages - 1); page++) {
    addPaginationLink(page, page,currentPage);
  }

  if (currentPage < totalPages - 2) {
    addEllipsis();
  }

  addPaginationLink(totalPages, totalPages,currentPage);

  if (currentPage < totalPages) {
    addPaginationLink('Next >', currentPage + 1,currentPage);
  }
}

function addPaginationLink(text, page,currentPage) {
  const li = document.createElement('li');
  const link = document.createElement('a');
  link.textContent = text;
  link.href = '#';
   if (page === currentPage) {
      link.classList.add('active');
    }
  link.addEventListener('click', function () {
    generatePaginationLinks(page, event);
  });

  li.appendChild(link);
  paginationList.appendChild(li);
}

function addEllipsis() {
  const li = document.createElement('li');
  li.classList.add('ellipsis');
  li.textContent = '...';
  paginationList.appendChild(li);
}


