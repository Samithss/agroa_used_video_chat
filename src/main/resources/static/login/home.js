  let APP_ID = "db553c1057e94a918046b4adcff4657b";

    var uid = String(Math.floor(Math.random() * 10000))
    let token = null;
    let client;

    let channel;

    var startTime;
    var endTime;

    let queryString = window.location.search
    let urlParams = new URLSearchParams(queryString)
    let roomId = urlParams.get('room')

    let localTracks = []
    let remoteUsers = {}
    document.getElementById("camera-btn").style.visibility = 'hidden';
    document.getElementById("mic-btn").style.visibility = 'hidden';
    document.getElementById("invite-user").style.visibility = 'hidden';
    document.getElementById("leave").style.visibility = 'hidden';

    var flagForUserInVideoCall = 0;
    let joinRoomInit = async () => {
      try {
        const email = await getSessionAttribute();
        uid = uid + email;
        console.log(uid);
        client = AgoraRTC.createClient({ mode: 'rtc', codec: 'vp8' });
        await client.join(APP_ID, roomId, token, uid);
        client.on('user-published', handleUserPublished);
        client.on('user-left', handleUserLeft);
        localTracks = await AgoraRTC.createMicrophoneAndCameraTracks({}, {
          encoderConfig: {
            width: { min: 640, ideal: 1920, max: 1920 },
            height: { min: 480, ideal: 1080, max: 1080 }
          }
        })

        document.getElementById("camera-btn").style.visibility = 'visible';
        document.getElementById("mic-btn").style.visibility = 'visible';
        document.getElementById("invite-user").style.visibility = 'visible';
        document.getElementById("leave").style.visibility = 'visible';
        document.querySelector(".profile-photo").style.visibility = 'hidden';

        let player = `<div class="remote__video__container" id="user-container-${uid}">
                            <div class="video-player" id="user-${uid}"></div>
                            <div class="user-name">${email}</div>
                      </div>`;

        flagForUserInVideoCall = 1;
        document.getElementById("remote").insertAdjacentHTML('beforeend', player);
        localTracks[1].play(`user-${uid}`);

        let displayFrame1 = document.getElementById('local');
        let videoFrames1 = document.getElementsByClassName('remote__video__container')
        let userInDisplayFrame1 = null;

        let expandVideoFrameUser = (e) => {
          let child = displayFrame1.children[0];
          if (child) {
            document.getElementById('remote').appendChild(child);
          }
          displayFrame1.appendChild(e.currentTarget);
          userIdInDisplayFrame = e.currentTarget.id;
        };
        expandVideoFrameUser({ currentTarget: document.getElementById(`user-container-${uid}`) });

        document.getElementById(`user-container-${uid}`).addEventListener('click', expandVideoFrame)
        await client.publish([localTracks[0], localTracks[1]]);
        document.getElementById("openModal").style.visibility = 'hidden';
        document.getElementById("openModal1").style.visibility = 'hidden';
        activeUser();
      } catch (error) {
      }
    }
    let handleUserPublished = async (user, mediaType) => {
      remoteUsers[user.uid] = user

      await client.subscribe(user, mediaType)

      let player = document.getElementById(`user-container-${user.uid}`)
      if (player === null) {
        player = `<div class="remote__video__container" id="user-container-${user.uid}">
                <div class="video-player" id="user-${user.uid}"></div>
                <div class="user-name">${user.uid}</div></div>`
        document.getElementById("remote").insertAdjacentHTML('beforeend', player);
        document.getElementById(`user-container-${user.uid}`).addEventListener('click', expandVideoFrame)
      }
      if (mediaType === 'video') {
        user.videoTrack.play(`user-${user.uid}`)
      }

      if (mediaType === 'audio') {
        user.audioTrack.play()
      }
    }

    let handleUserLeft = async (user) => {
      delete remoteUsers[user.uid]
      let item = document.getElementById(`user-container-${user.uid}`)
      if (item) {
        item.remove()
      }
    }
    let toggleMic = async (e) => {
      let button = e.currentTarget

      if (localTracks[0].muted) {
        await localTracks[0].setMuted(false)
        document.getElementById('mic-btn').style.backgroundColor = 'rgb(179, 102, 249, .9)';
      } else {
        await localTracks[0].setMuted(true)
        document.getElementById('mic-btn').style.backgroundColor = 'rgb(255, 80, 80)';
      }
    }

    let toggleCamera = async (e) => {
      if (localTracks[1].muted) {
        await localTracks[1].setMuted(false)
        document.getElementById('camera-btn').style.backgroundColor = 'rgb(179, 102, 249, .9)';
      } else {
        await localTracks[1].setMuted(true)
        document.getElementById('camera-btn').style.backgroundColor = 'rgb(255, 80, 80)';
      }
    }


    let displayFrame = document.getElementById('local');
    let videoFrames = document.getElementsByClassName('remote__video__container')
    let userInDisplayFrame = null;

    let expandVideoFrame = (e) => {
      let child = displayFrame.children[0];
      if (child) {
        document.getElementById('remote').appendChild(child);
      }
      displayFrame.appendChild(e.currentTarget);
      userIdInDisplayFrame = e.currentTarget.id
    }



    let leaveStream = async (e) => {
      flagForUserInVideoCall = 0;
      e.preventDefault()
      document.getElementById("openModal").style.visibility = 'visible';
      document.getElementById("openModal1").style.visibility = 'visible';
      document.querySelector(".profile-photo").style.visibility = 'visible';
      document.getElementById("camera-btn").style.visibility = 'hidden';
      document.getElementById("mic-btn").style.visibility = 'hidden';
      document.getElementById("invite-user").style.visibility = 'hidden';
      document.getElementById("leave").style.visibility = 'hidden';


      for (let i = 0; localTracks.length > i; i++) {
        localTracks[i].stop()
        localTracks[i].close()
      }

      await client.unpublish([localTracks[0], localTracks[1]])
      document.getElementById(`user-container-${uid}`).remove();
      leaveChannel();
      window.location.href = "home";
    }

    let getSessionAttribute = () => {
      return new Promise((resolve, reject) => {
        var xhr = new XMLHttpRequest();
        xhr.onload = function () {
          if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
              var response = JSON.parse(xhr.responseText);
              var email = response.email;
              console.log("User Email from Session:", email);
              resolve(email);
            } else {
              console.error("Failed to retrieve session attribute");
              reject(new Error("Failed to retrieve session attribute"));
            }
          }
        }
        xhr.open("GET", "/get-session", true);
        xhr.send();
      });
    }
    function leaveChannel() {
      var currentDateTimeForUserLeft = new Date();
      var e_year = currentDateTimeForUserLeft.getFullYear();
      var e_month = currentDateTimeForUserLeft.getMonth();
      var e_day = currentDateTimeForUserLeft.getDate();
      var e_hours = currentDateTimeForUserLeft.getHours();
      var e_minutes = currentDateTimeForUserLeft.getMinutes();
      var e_seconds = currentDateTimeForUserLeft.getSeconds();
      endTimeOfUserLeft = e_day + '/' + (e_month + 1) + '/' + e_year + ' ' + e_hours + ':' + e_minutes + ':' + e_seconds;
      const currentURL = window.location.href;
      const url = new URL(currentURL);
      let roomId = "null";
      roomId = url.searchParams.get('room');

      const xhttp = new XMLHttpRequest();
      xhttp.onload = function () {
        if (xhttp.readyState === XMLHttpRequest.DONE) {
          if (xhttp.status === 200) {
            console.log(xhttp.responseText);
            var response = JSON.parse(xhttp.responseText);
            console.log(response);
          }
          else {
            console.log("Request failed");
          }
        }
      };
      const data = {
        param1: roomId,
        param2: email,
        param3: endTimeOfUserLeft
      };
      xhttp.open("POST", "/userLeftTheRoom", true);
      xhttp.setRequestHeader("Content-type", "application/json");
      xhttp.send(JSON.stringify(data));
    }

    var inviteCodeToJoin;
    var email;
      document.addEventListener("DOMContentLoaded", function () {
      const openModalButton = document.getElementById("openModal");
      const closeModalButton = document.getElementById("closeModal");
      const modal = document.getElementById("modal");
      try { openModalButton.addEventListener("click", openModal); }
      catch (error) {
      }
      function openModal() {
        modal.style.display = "block";
        var currentDate = new Date();
        var year = currentDate.getFullYear();
        var month = currentDate.getMonth(); // 0-11 (January is 0)
        var day = currentDate.getDate();
        var hours = currentDate.getHours();
        var minutes = currentDate.getMinutes();
        var seconds = currentDate.getSeconds();
        startTime = day + '/' + (month + 1) + '/' + year + ' ' + hours + ':' + minutes + ':' + seconds;
        detailsOfStartRoom(startTime, email);
      }
      try {
        closeModalButton.addEventListener("click", () => {
          modal.style.display = "none";
        });
      }
      catch (error) { }

      window.addEventListener("click", (event) => {
        if (event.target === modal) {
          modal.style.display = "none";
        }
      });
      let form = document.getElementById('join-form')
      form.addEventListener('submit', async (e) => {
        e.preventDefault()
        let inviteCode = e.target.invite_link.value
        inviteCodeToJoin = inviteCode;
        console.log("iddd" + inviteCode);
        detailsOfUserInRoom(inviteCode, email)
        window.location = `home?room=${inviteCode}`;
      })
      joinRoomInit();
    });
    var email; //line in 312
    var emailOfUser;
    getSessionAttributeNew();


    function getSessionAttributeNew() {
      var xhr = new XMLHttpRequest();
      xhr.onload = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            email = response.email;
            emailOfUser = email;
            getIfNotification();
            console.log("User Email from Session:", email);
          } else {
            console.error("Failed to retrieve session attribute");
          }
        }
      }
      xhr.open("GET", "/get-session", true);
      xhr.send();
    }


    function detailsOfUserInRoom(inviteCode, email) {
      const xhttp = new XMLHttpRequest();
      var currentDateOfUserJoined = new Date();
      var year = currentDateOfUserJoined.getFullYear();
      var month = currentDateOfUserJoined.getMonth(); // 0-11 (January is 0)
      var day = currentDateOfUserJoined.getDate();
      var hours = currentDateOfUserJoined.getHours();
      var minutes = currentDateOfUserJoined.getMinutes();
      var seconds = currentDateOfUserJoined.getSeconds();
      let startTimeOfUserJoined = day + '/' + (month + 1) + '/' + year + ' ' + hours + ':' + minutes + ':' + seconds;
      xhttp.onload = function () {
        if (xhttp.readyState === XMLHttpRequest.DONE) {
          if (xhttp.status === 200) {
            console.log(xhttp.responseText);
          } else {
            console.error("Request failed:", xhttp.status);
          }
        }
      };
      const data = {
        param1: email,
        param2: inviteCode,
        param3: startTimeOfUserJoined
      };
      xhttp.open("POST", "/detailsOfUserInRoom", true);
      xhttp.setRequestHeader("Content-type", "application/json"); // Set the content type to JSON
      xhttp.send(JSON.stringify(data)); // Convert the data object to a JSON string
    }

    function detailsOfStartRoom(startTime, email) {
      const xhttp = new XMLHttpRequest();
      xhttp.onload = function () {
        if (xhttp.readyState === XMLHttpRequest.DONE) {
          if (xhttp.status === 200) {
            console.log(xhttp.responseText);
            var response = JSON.parse(xhttp.responseText);
            console.log(response);
            console.log(response.result);
            let inviteCodeInput = document.getElementById("inviteCodeInput");
            inviteCodeInput.value = response.result;

          } else {
            console.error("Request failed:", xhttp.status);
          }
        }
      };
      const data = {
        param1: email,
        param3: startTime
      };
      xhttp.open("POST", "/detailsOfStartRoom", true);
      xhttp.setRequestHeader("Content-type", "application/json");
      xhttp.send(JSON.stringify(data));
      return roomId;
    }

    document.addEventListener("DOMContentLoaded", function () {
      const openModalButton = document.getElementById("openModal1");
      const closeModalButton = document.getElementById("closeModal1");
      const modal = document.getElementById("modal1");

      try {
        openModalButton.addEventListener("click", () => {
          modal.style.display = "block";
        });
      }
      catch (error) {
      }

      try {
        closeModalButton.addEventListener("click", () => {
          modal.style.display = "none";
        });
      }
      catch (error) {
      }

      window.addEventListener("click", (event) => {
        if (event.target === modal) {
          modal.style.display = "none";
        }
      });
      let form = document.getElementById('join-form1')
      form.addEventListener('submit', async (e) => {
        e.preventDefault()
        let inviteCode = e.target.invite_link_to_join.value
        await userJoinedToRoom(inviteCode, email);
      })

    });
    async function joinToVideoCallFromNotification() {
      let paragraphText = document.getElementById("invitedDetails").textContent;
      let code = paragraphText.match(/CODE (\w+)/)[1];
      await userJoinedToRoom(code, email);
    }

    async function userJoinedToRoom(inviteCode, email) {
      var currentDateOfRemoteUserJoined = new Date();
      var year = currentDateOfRemoteUserJoined.getFullYear();
      var month = currentDateOfRemoteUserJoined.getMonth(); // 0-11 (January is 0)
      var day = currentDateOfRemoteUserJoined.getDate();
      var hours = currentDateOfRemoteUserJoined.getHours();
      var minutes = currentDateOfRemoteUserJoined.getMinutes();
      var seconds = currentDateOfRemoteUserJoined.getSeconds();
      let startTimeOfRemoteUserJoined = day + '/' + (month + 1) + '/' + year + ' ' + hours + ':' + minutes + ':' + seconds;
      let status = 1;
      const xhttp = new XMLHttpRequest();
      xhttp.onload = function () {
        if (xhttp.readyState === XMLHttpRequest.DONE) {
          if (xhttp.status === 200) {
            console.log(xhttp.responseText);
            var response = JSON.parse(xhttp.responseText);
            if (response.result !== "error" && response.result !== "Full") {
              window.location = `home?room=${inviteCode}`;
              joinRoomInit();
            } else if (response.result === "error") {
              console.log("Request failed");
              alert("No room with the provided code was found.");
            } else if (response.result === "Full") {
              console.log("Request failed");
              alert("Room is full");
            }
          }
        }
      };
      const data = {
        param1: inviteCode,
        param2: email,
        param3: status,
        param4: startTimeOfRemoteUserJoined
      };
      xhttp.open("POST", "/userJoinedToRoom", true);
      xhttp.setRequestHeader("Content-type", "application/json"); // Set the content type to JSON
      xhttp.send(JSON.stringify(data)); // Convert the data object to a JSON string
    }
    function copyText() {
      var copyText = document.getElementById("inviteCodeInput");
      copyText.select();
      copyText.setSelectionRange(0, 99999);
      navigator.clipboard.writeText(copyText.value);
      alert("Copied the text: " + copyText.value);
    }

    let inviteCodeInput = document.getElementById("inviteCodeInput");
    let inviteCode = ""
    const cancelBtn = document.getElementById("cancel");
    try {
      cancelBtn.addEventListener("click", function () {
        inviteCode = inviteCodeInput.value;
        var endDateTime = new Date();
        var year = endDateTime.getFullYear();
        var month = endDateTime.getMonth(); // 0-11 (January is 0)
        var day = endDateTime.getDate();
        var hours = endDateTime.getHours();
        var minutes = endDateTime.getMinutes();
        var seconds = endDateTime.getSeconds();
        endTime = day + '/' + (month + 1) + '/' + year + ' ' + hours + ':' + minutes + ':' + seconds;
        const xhttp = new XMLHttpRequest();
        xhttp.onload = function () {
          if (xhttp.readyState === XMLHttpRequest.DONE) {
            if (xhttp.status === 200) {
            }
          }
        };
        const data = {
          param1: inviteCode,
          param2: endTime
        };
        xhttp.open("POST", "/endTime", true);
        xhttp.setRequestHeader("Content-type", "application/json");
        xhttp.send(JSON.stringify(data));
        window.location.href = "home";
      });
    } catch (error) {
    }

    let divEle = document.getElementById('labelForUserInCall');
    const pEle = document.createElement("p");
    pEle.textContent = "PARTICIPANTS";
       divEle.appendChild(pEle);
     let nameOfLabel=document.getElementById('nameOfLabel');
            nameOfLabel.innerHTML=" ";
            const p1 = document.createElement("p");
            p1.textContent = "ACTIVE USER";
           nameOfLabel.appendChild(p1);


    function adjustScreen() {
      if (document.querySelector('.main-videoCall-container').style.width == "70%") {
        let div = document.getElementById('labelForUserInCall');
        div.innerHTML = "";
        let div1 = document.getElementById('userInCall');
        div1.innerHTML = "";
        let nameOf=document.getElementById('nameOfLabel');
        nameOf.innerHTML="";
        document.getElementById('activeUser').style.display = 'none';
        document.getElementById('buttonload').style.display = 'none';
        let di = document.getElementById('labelForUserInCall');
        di.innerHTML = " ";
        document.querySelector('.main-videoCall-container').style.width = "100%";
        document.getElementById('inviteAndOnline').style.width = "0%";
      } else {
        activeUser();
        userInCall();
        let div2 = document.getElementById('labelForUserInCall');
        div2.innerHTML = " ";
        const p = document.createElement("p");
        p.textContent = "PARTICIPANTS";
        div2.appendChild(p);
        let nameOfLabel=document.getElementById('nameOfLabel');
        nameOfLabel.innerHTML=" ";
        const p1 = document.createElement("p");
        p1.textContent = "ACTIVE USER";
        nameOfLabel.appendChild(p1);
        document.getElementById('activeUser').style.display = 'block';
        document.querySelector('.main-videoCall-container').style.width = "70%";
        document.getElementById('inviteAndOnline').style.width = "30%";
        document.getElementById('buttonload').style.display = 'block';
      }
    }

    // access of session
    // Call the fuactiveUsernction to retrieve the session attribute
    // Call getIfNotification every 5 seconds
    setInterval(getIfNotification, 4000); // 5000 milliseconds = 5 seconds

    document.getElementById('buttonload').style.visibility = 'hidden';
    function activeUser() {
      document.getElementById('buttonload').style.visibility = 'visible';
      clearrr();
      const xhttp = new XMLHttpRequest();
      xhttp.onload = function () {
        console.log(this.responseText);
        document.getElementById('buttonload').style.visibility = 'hidden';
        document.getElementById('buttonload').style.height = '0px';
        const obj = JSON.parse(this.responseText)
        console.log(obj)
        const infoList = document.getElementById("value");
        obj.forEach(item => {
          const userId = item.userPresence.userId;
          const userEmail = item.userEmail;
          console.log("User ID:", userId);
          console.log("User Email:", userEmail);
          //                  const li = document.createElement("li");
          var divEle = document.createElement('div');
          divEle.classList.add('griDiv');
          divEle.innerHTML = `${item.userEmail}`;
          if (flagForUserInVideoCall === 1) {
            const btn = document.createElement('button')
            btn.classList.add("buttonsForInvite")
            //                    btn.id = "myButton";
            btn.addEventListener('click', function () {
              alert("Notification sent");
              putData(userEmail, email, inviteCodeToJoin);
            });
            btn.appendChild(document.createTextNode("INVITE"));
            divEle.appendChild(btn)
          }
          infoList.appendChild(divEle);
        });
      }
      xhttp.open("GET", "/activeUser/fetch");
      xhttp.send();
    }


    function clearrr() {
      const valueDiv = document.getElementById('value');

      while (valueDiv.firstChild) {
        valueDiv.removeChild(valueDiv.firstChild);
      }
    }

    function putData(userEmail, email, inviteCodeToJoin) {
      const currentURL = window.location.href;
      const url = new URL(currentURL);
      let roomId = "null";
      roomId = url.searchParams.get('room');
      console.log('Room ID:', roomId);
      const xhttp = new XMLHttpRequest();
      xhttp.onload = function () {
        if (xhttp.readyState === XMLHttpRequest.DONE) {
          if (xhttp.status === 200) {
            console.log(xhttp.responseText);
          } else {

            console.error("Request failed with status:", xhttp.status);
          }
        }
      };
      const data = {
        param1: userEmail,
        param2: email,
        param3: roomId
      };
      xhttp.open("POST", "/putInviteDetails", true);
      xhttp.setRequestHeader("Content-type", "application/json"); // Set the content type to JSON
      xhttp.send(JSON.stringify(data)); // Convert the data object to a JSON string
    }
    var x;
    var y;
    var sender;
    var receiver;
    function getIfNotification() {
      var xhr = new XMLHttpRequest();
      xhr.onload = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            const obj = JSON.parse(this.responseText);
            console.log(obj);
            var text;
            var p;
            obj.forEach(item => {
              sender = item.sender;
              receiver = item.receiver;
              try {
                x = document.getElementById("snackbar");
                x.className = "show";
                let content = document.getElementById('invitedDetails');
                content.innerHTML = item.sender + " INVITED YOU USE THIS CODE " + item.inviteCode + " TO JOIN";
              } catch (error) {
              }
              try {
                y = document.getElementById("snackbarProf");
                y.className = "show";
                let profContent = document.getElementById('invitedDetailsProf');
                profContent.innerHTML = item.sender + " INVITED YOU USE THIS CODE " + item.inviteCode + " TO JOIN";
              } catch (error) {
              }
            });

            deleteNotification();

          }
        }
      }
      const data1 = {
        parameter1: emailOfUser
      };
      xhr.open("POST", "/get-notification-details", true);
      xhr.setRequestHeader("Content-type", "application/json"); // Set the content type to JSON
      xhr.send(JSON.stringify(data1)); // Convert the data object to a JSON string
    }
    function deleteNotificationBar() {
      x.className = x.className.replace("show", "");
    }
    function deleteNotificationBarProf() {
      y.className = y.className.replace("show", "");
    }

    function deleteNotification() {
      var xhr = new XMLHttpRequest();
      xhr.onload = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            console.log("done");
          }
        }
      }
      const data1 = {
        parameter1: sender,
        parameter2: receiver
      };
      xhr.open("POST", "/delete-notification-details", true);
      xhr.setRequestHeader("Content-type", "application/json"); // Set the content type to JSON
      xhr.send(JSON.stringify(data1)); // Convert the data object to a JSON string
    }

    function copyTextValue() {
      let paragraphText = document.getElementById("invitedDetails").textContent;
      let code = paragraphText.match(/CODE (\w+)/)[1];
      let tempInput = document.createElement("input");
      tempInput.value = code;
      document.body.appendChild(tempInput);
      tempInput.select();
      tempInput.setSelectionRange(0, 99999);
      document.execCommand("copy");
      document.body.removeChild(tempInput);
      return code;
    }

    function copyTextValueProf() {
      let paragraphText = document.getElementById("invitedDetailsProf").textContent;
      let code = paragraphText.match(/CODE (\w+)/)[1];
      let tempInput = document.createElement("input");
      tempInput.value = code;
      document.body.appendChild(tempInput);
      tempInput.select();
      tempInput.setSelectionRange(0, 99999);
      document.execCommand("copy");
      document.body.removeChild(tempInput);
      alert("Code copied to clipboard: " + code);
    }

    document.getElementById('camera-btn').addEventListener('click', toggleCamera)
    document.getElementById('mic-btn').addEventListener('click', toggleMic)
    document.getElementById('invite-user').addEventListener('click', adjustScreen)
    document.getElementById('leave').addEventListener('click', leaveStream)
    const currentURL = window.location.href;
    const url = new URL(currentURL);
    var roomCode = "null";
    roomCode = url.searchParams.get('room');



    function userInCall() {
      const xhr = new XMLHttpRequest();

      xhr.onload = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          if (xhr.status === 200) {
            const responseArray = JSON.parse(this.responseText);
            const userInCallDiv = document.getElementById("userInCall");
            userInCallDiv.innerHTML = "";
            responseArray.forEach(email => {
              const div = document.createElement("div");
              div.classList.add('gridDivParticipants');
              div.textContent = email;
              const letterDiv = document.createElement("div");
              letterDiv.classList.add('roundDiv');
              letterDiv.textContent = email[0].toUpperCase(); // Display the first letter of the email
              div.appendChild(letterDiv);
              userInCallDiv.appendChild(div);
            });

          }
        }
      };
      const data1 = {
        parameter1: roomCode,
      };
      xhr.open("POST", "/get-userInCall", true);
      xhr.setRequestHeader("Content-type", "application/json"); // Set the content type to JSON
      xhr.send(JSON.stringify(data1)); // Convert the data object to a JSON string
    }
