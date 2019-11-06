const functions = require('firebase-functions');
// const http = require('http');


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
   exports.updateUser = functions.firestore.document('sound_sensor_values/{soundId}').onCreate((snap, context) => {
      // Get an object representing the document
	      // e.g. {'name': 'Marie', 'age': 66}
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
     db.doc("dublin-sensor-data/1").set({
      date: zip[0],
      time: zip[1],
      value: zip[2]
      })
     .then(function() {
         console.log("Document successfully written!");
	 return null;
      })
     .catch(function(error) {
         console.Error("error writing document: ", error);
	 return null;
      });

   }

   
   var urlLatest= "http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php"
   var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
   var xmlHttp = new XMLHttpRequest();
   xmlHttp.addEventListener("load", reqListener);
   xmlHttp.open("GET", urlLatest, false); // false for synchronous request
   xmlHttp.send();
	
   return 0;

 });
  
//exports.cronSoundData = functions.pubsub.schedule('0 * * * *').onRun((context) => {


  exports.cronSoundData = functions.pubsub.schedule('5 8 * * 0').onRun((context) => {

    const runtimeOpts = {
       timeoutSeconds: 50000,
       memory: '1GB'
    }
     console.log('hello world');
     var http = require('http');
     console.log('http');
     var req = http.get('http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php', (res) => {
        console.log('sent http request');
        // console.log(res);
        const { statusCode } = res;
        const contentType = res.headers['content-type'];
        let error;
        if (statusCode !== 200) {
          error = new Error('Request Failed.\n' +
                            `Status Code: ${statusCode}`);
        } 
        if (error) {
          console.error(error.message);
          // Consume response data to free up memory
          res.resume();
          return;
        }
        res.setEncoding('utf8');
        let rawData = '';
        res.on('data', (chunk) => { rawData += chunk; });
        res.on('end', () => {
        try {
          const parsedData = JSON.parse(rawData);
          handleData(parsedData);
    
          // console.log(parsedData);
        } catch (e) {
            console.error(e.message);
          }
        });
      // console.log(res)
      // return res;
    }).on('error', (e) => {
      console.error(`Got error: ${e.message}`);
    });
    req.end();

// exports.cronSoundData = functions.pubsub.schedule('5 8 * * 0').onRun((context) => {
//    // import the module
//    var request = require('request');

//    // make the request
//    request('http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php', function (error, response, body) {
//       if (!error && response.statusCode === 200) {
//          //here put what you want to do with the request
      
   // console.log('hello world');
   // var http = require('http');
   // console.log('http');
   // var req = http.get('http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php', (res) => {
        // console.log('sent http request');
         // console.log(res);
   //       const { statusCode } = response;
   //       const contentType = response.headers['content-type'];
   //       let error;
   //       if (statusCode !== 200) {
   //       error = new Error('Request Failed.\n' +
   //                         `Status Code: ${statusCode}`);
   //       } 
   //       if (error) {
   //          console.error(error.message);
   //          // Consume response data to free up memory
   //          response.resume();
   //          return;
   //       }
   //       response.setEncoding('utf8');
   //       let rawData = '';
   //       response.on('data', (chunk) => { rawData += chunk; });
   //       response.on('end', () => {
   //       try {
   //          const parsedData = JSON.parse(rawData);
   //          handleData(parsedData);
   
   //       // console.log(parsedData);
   //       } catch (e) {
   //          console.error(e.message);
   //       }
   //    });
   // }
//})
    // console.log(res)
    // return res;
//   }).on('error', (e) => {
//     console.error(`Got error: ${e.message}`);
//   });
//   req.end();

	      // e.g. {'name': 'Marie', 'age': 66}
//      function zip(arrays) {
//         return arrays[0].map(function(_,i){
//            return arrays.map(function(array){return array[i]})
//         });
//      }

//    function sendRequest(){
//       const http = require('http');
//       http.get('http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php', (res) => {
//            const { statusCode } = res;
//            const contentType = res.headers['content-type'];

//            let error;
//            if (statusCode !== 200) {
//            error = new Error('Request Failed.\n' +
//                       `Status Code: ${statusCode}`);
//            } 
//           if (error) {
//              console.error(error.message);
//     // Consume response data to free up memory
//              res.resume();
//              return;
//           }
//           res.setEncoding('utf8');
//           let rawData = '';
//           res.on('data', (chunk) => { rawData += chunk; });
//      	  res.on('end', () => {
//             try {
//               const parsedData = JSON.parse(rawData);
//               console.log(parsedData);
//             } catch (e) {
//               console.error(e.message);
//             }
//          });
//  // console.log(res)
//          return res;
//       }).on('error', (e) => {
//            console.error(`Got error: ${e.message}`);
//       });
      
//    }

//    function reqListener () {
//      console.log('response recieved')
// //     console.log(this.responseText);
//      var response = this.responseText;
//      console.log('parsing JSON');
//      var obj = JSON.parse(response);
//      console.log('parsed JSON')
//      var i =0;
//      var arrays = []
//      for(var key in obj){
//        if (obj.hasOwnProperty(key)){
//           var value=obj[key];
// 	  console.log('key: '+key+'\nvalue: '+value);
// 	  arrays.push(value)
//        }
//     }
//     console.log('zipping data')
//     var zipped = zip(arrays.slice(0,3))
//     console.log(zipped)
//     zipped.forEach(item => {
// 	    console.log('logging each item in zipped')
// 	    console.log(item)
// 	    writeToDb(item)
//     });
//     console.log('end of reqListener')
//    }
   
   function  writeToDb (zip){
     const admin = require('firebase-admin');
     admin.initializeApp();
     const db = admin.firestore();
     console.log('writing zip item to db')
     db.doc("dublin-sensor-data/1").set({
      date: zip[0],
      time: zip[1],
      value: zip[2]
      })
     .then(function() {
         console.log("Document successfully written!");
	 return null;
      })
     .catch(function(error) {
         console.log("error writing document: ", error);
	 return null;
      });

   }

   // function handleData(response){
   // //   var obj = JSON.parse(response);
   //   console.log('parsed JSON')
   //   var i =0;
   //   var arrays = []
   //   for(var key in response){
   //     if (response.hasOwnProperty(key)){
   //        var value=response[key];
	//   console.log('key: '+key+'\nvalue: '+value);
	//   arrays.push(value)
   //     }
   //  }
   //  console.log('zipping data')
   //  var zipped = zip(arrays.slice(0,3))
   //  console.log(zipped)
   //  zipped.forEach(item => {
	//     console.log('logging each item in zipped')
	//     console.log(item)
	//     writeToDb(item)
   //  });
   // }
   // console.log('creating http request')
   // var urlLatest= "http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php"
   // var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
   // var xmlHttp = new XMLHttpRequest();
   // xmlHttp.addEventListener("load", reqListener);
   // xmlHttp.open("GET", urlLatest); // false for synchronous request
   // xmlHttp.send();
   // var response = sendRequest();
   function handleData(response){
      //   var obj = JSON.parse(response);
        var i =0;
        var arrays = []
        console.log('response in handleData:')
        // console.log(response)
        console.log('logging values in response');
        for(var key in response){
          if (response.hasOwnProperty(key)){
             var value=response[key];
             console.log('key: '+key+'\nvalue: '+value);
             arrays.push(value)
          }
          else {
            console.log('idk');
          }
       }
       console.log('zipping data')
       var zipped = zip(arrays.slice(0,3))
       console.log(zipped)
       zipped.forEach(item => {
         console.log('logging each item in zipped')
         console.log(item)
         writeToDb(item)
       });
      }
    
        // e.g. {'name': 'Marie', 'age': 66}
        function zip(arrays) {
          return arrays[0].map(function(_,i){
             return arrays.map(function(array){return array[i]})
          });
       }
    
       
    
   // handleData(response);   
   // console.log('sent http request');
   console.log('end');
   return 0; 

   });

//
//export scheduledFunctionPlainEnglish =
//functions.pubsub.schedule('every 5 minutes').onRun((context) => {
  //  console.log('This will be run every 5 minutes!');
//});

