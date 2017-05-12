import * as Manager from 'ranttu.rapid.jsvm.test.example.threadasync.ThreadAsyncManager';

var man = new Manager();
export var result;

export var promise =

man.instanceTask()
.then(function(ret) {
    result = ret;
    return result;
});