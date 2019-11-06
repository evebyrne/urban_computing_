const functions = require('firebase-functions');
const http = require('http');
//const admin = require('firebase-admin');
//admin.initializeApp();
//const db = admin.firestore();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
 //  exports.updateUser = functions.firestore.document('sound_sensor_values/{soundId}').onCreate((snap, context) => {
      // Get an object representing the document
	      // e.g. {'name': 'Marie', 'age': 66}
   //  function zip(arrays) {
    //return arrays[0].map(function(_,i){
      //  return arrays.map(function(array){return array[i]})
    //});
   //}
  exports.cronSoundData = functions.pubsub.schedule('*****').onRun(() => {
      // Get an object representing the document
	      // e.g. {'name': 'Marie', 'age': 66}
     //function zip(arrays) {
     console.log('hello world')
    //return arrays[0].map(function(_,i){
      //  return arrays.map(function(array){return array[i]})
    //});
     return 0;
   })

  
