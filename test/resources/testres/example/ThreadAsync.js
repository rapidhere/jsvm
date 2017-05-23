import * as Manager from 'ranttu.rapid.jsvm.test.testrt.ThreadAsyncManager';

var man = new Manager();
export var result;

export var promise =

man.instanceTask()
.then(function(ret) {
    result = ret;
    return result;
});