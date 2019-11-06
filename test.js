const http = require('http');
const firebase = require("firebase");
const admin = require('firebase-admin');
admin.initializeApp();
const db = admin.firestore();


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
      // Get an object representing the document
	      // e.g. {'name': 'Marie', 'age': 66}
	//const newValue = snap.data();

	      // access a particular field as you would any JS property
	      //const name = newValue.name;

	      // perform desired operations ...
   function zip(arrays) {
    return arrays[0].map(function(_,i){
        return arrays.map(function(array){return array[i]})
    });
   }

   function reqListener () {
     console.log(this.responseText);
     var response = this.responseText;
     var obj = JSON.parse(response);
     var i =0;
     var arrays = []
     for(var key in obj){
       if (obj.hasOwnProperty(key)){
          var value=obj[key];
	  console.log('key: '+key+'\nvalue: '+value);
	  arrays.push(value)
       }
    }
    

   var zipped = zip(arrays.slice(0,3))
    console.log(zipped)
    zipped.forEach(item => {
	    console.log(item)
	    writeToDb(item)
    });
   }
   function  writeToDb (zip){
	    //db.doc('some/otherdoc').set({ ... });

     db.doc("dublin-sensor-data/1").set({
      date: zip[0],
      time: zip[1],
      value: zip[2]
      })
     .then(function() {
         console.log("Document successfully written!");
      })
     .catch(function(error) {
         console.error("Error writing document: ", error);
      });

   }

   
   var urlLatest= "http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php" 
   var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
   var xmlHttp = new XMLHttpRequest();
   xmlHttp.addEventListener("load", reqListener);
   xmlHttp.open("GET", urlLatest); // false for synchronous request
   xmlHttp.send();
