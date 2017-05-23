function entry() {
    var count = 500000;
    var i = 0;
    var fieldName;
    var t = 0;
    var obj = {
        field1: 1,
        field2: 2,
        field3: 3,
        field4: 4
    };

    while(count) {
        count = count - 1;
        i = i + 1;
        if (i === 4) {
            i = 0;
        }

        if(i === 0) {
            fieldName = 'field1';
        } else if (i === 1) {
            fieldName = 'field2';
        } else if(i === 2) {
            fieldName = 'field3';
        } else if(i === 3) {
            fieldName = 'field4';
        }

        t = t + obj[fieldName];
    }

    return t;
}