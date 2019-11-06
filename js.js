

function getData(){
  console.log('sending http request');
  
  console.log('end of fxn');
}

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
   function all(){
      const http = require('http');
      http.get('http://dublincitynoise.sonitussystems.com/applications/api/dublinnoisedata.php', (res) => {
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
   }

   all();

