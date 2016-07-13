var TaskRepository = function() {
  var urlbase = 'http://localhost:4567/api/v1/jsonp';
  
  var getTaskList = function(vue, token, success, error) {
    vue.$http.jsonp(`${urlbase}/task/list?token=${token}`).then(
      (data, status, request) => success(data.data, status),
      () => error(data, status)
    );
  };

  var addTask = function(vue, token, name, enddate, success, error) {
    vue.$http.jsonp(`${urlbase}/task/add?token=${token}&name=${name}&enddate=${enddate}`).then(
      (data, status, request) => success(data.data, status),
      (data, status, request) => error(data, status)
    );
  };

  var done = function(vue, token, taskId, success, error) {
    vue.$http.jsonp(`${urlbase}/task/add?token=${token}&name=${name}&enddate=${enddate}`).then(
      (data, status, request) => success(data.data, status),
      (data, status, request) => error(data, status)
    );
  }



  return {
    getTaskList: getTaskList,
    addTask: addTask
  };
};
