window.onload = function () {
    this.initTaskCountChart();
    this.initComputerStatus();
    const client = new WebsocketClient('/endpoint', (res) => {
        if (res.action === 'onopen') {
            this.loadComputer();
            this.loadTask();
        }
        if (res.action === 'onclose') {
            alert('服务端出现错误！');
            window.location.reload();
        }
        if (res.action === 'onmessage') {
            const body = JSON.parse(res.body);
            const {action} = body;
            if (action === 'loadComputer') {
                this.loadComputerResult(body)
            }
            if (action === 'loadTask') {
                this.loadTaskResult(body)
            }
            if (action === 'addComputer') {
                this.addComputerResult(body)
            }
            if (action === 'addTask') {
                this.addTaskResult(body)
            }
            if (action === 'selectComputerById') {
                this.showComputerOneResult(body)
            }
        }
    });
    client.connect();
    window.client = client;
};

function loadComputer() {
    showLoad('computer-manager');
    const request = {
        action: 'loadComputer'
    };
    client.send(JSON.stringify(request));
}

function loadComputerResult(res) {
    const {data, success, msg} = res;
    if (!success) {
        console.error(msg);
        return;
    }
    way.set("computer.list", data);

    this.initCpuTop5(data);
    this.initMemoryTop5(data);
    this.initDiskTop5(data);
    this.initNetworkTop5(data);

    // 加载显示
    if (data.length > 0) {
        this.showComputerOne(data[0].id);
    }
    hideLoad('computer-manager')
}

// 对计算机进行按照cpu占用率排序
function initCpuTop5(data) {
    const list = [...data];
    list.sort((a, b) => {
        return b.cpuUsage - a.cpuUsage;
    });
    // 只显示前8
    way.set("computer.cpuTopList", list.slice(0, 8));

    this.initProgress('task-cpu-top')
}

// 对计算机进行按照内存占用率排序
function initMemoryTop5(data) {
    const list = [...data];
    list.sort((a, b) => {
        return b.memoryUsage - a.memoryUsage;
    });
    // 只显示前8
    way.set("computer.memoryTopList", list.slice(0, 8));

    this.initProgress('task-memory-top')
}

// 对计算机进行按照硬盘占用率排序
function initDiskTop5(data) {
    const list = [...data];
    list.sort((a, b) => {
        return b.diskUsage - a.diskUsage;
    });
    // 只显示前8
    way.set("computer.diskTopList", list.slice(0, 8));

    this.initProgress('task-disk-top')
}
// 对计算机进行按照带宽占用率排序
function initNetworkTop5(data) {
    const list = [...data];
    list.sort((a, b) => {
        return b.networkUsage - a.networkUsage;
    });
    // 只显示前8
    way.set("computer.networkTopList", list.slice(0, 8));

    this.initProgress('task-network-top')
}

// 设置进度条
function initProgress(id) {
    for (let i = 0; i < 8; i++) {
        const div = document.getElementById(id + '-' + i);
        const num = div.innerHTML;
        const percent = 100 - Number(num);
        div.style.width = percent + '%'
    }
}

function loadTask() {
    showLoad('task-manager');
    const request = {
        action: 'loadTask'
    };
    client.send(JSON.stringify(request));
}

function loadTaskResult(res) {
    const {data, success, msg} = res;
    if (!success) {
        console.error(msg);
        return;
    }
    way.set("task.list", data);
    hideLoad('task-manager')
}

function showAddComputerDialog() {
    const computerDialog = document.getElementById('computer-dialog');
    if (computerDialog) {
        computerDialog.style.display = 'block'
    }
}

function doAddComputer() {
    const formData = way.get("computer.form");
    if (!formData || !formData.name || !formData.cpu || !formData.memory || !formData.disk || !formData.network) {
        alert("必填项不可为空");
        return;
    }
    showLoad('computer-dialog');
    const request = {
        action: 'addComputer',
        data: formData
    };
    client.send(JSON.stringify(request));
}

function addComputerResult(res) {
    hideLoad('computer-dialog');
    const {success} = res;
    if (!success) {
        alert('添加失败！');
        return;
    }
    way.remove("computer.form");
    closeDialog();
    loadComputer();
}

function showAddTaskDialog() {
    const computerDialog = document.getElementById('task-dialog');
    if (computerDialog) {
        computerDialog.style.display = 'block'
    }
}

function doAddTask() {
    const formData = way.get("task.form");
    console.log(formData);
    if (!formData || !formData.name || !formData.cpuUsage || !formData.memoryUsage || !formData.diskUsage || !formData.networkUsage || !formData.timeUsage) {
        alert("必填项不可为空");
        return;
    }
    showLoad('task-dialog');
    const request = {
        action: 'addTask',
        data: formData
    };
    client.send(JSON.stringify(request));
}

function addTaskResult(res) {
    hideLoad('task-dialog');
    const {success} = res;
    if (!success) {
        alert('添加失败！');
        return;
    }
    way.remove("task.form");
    closeDialog();
    loadTask();
}


function showComputer(index) {
    const data = way.get("computer.list") || [];
    if (data && data.length > 0) {
        const {id} = data[index];
        if (id) {
            this.showComputerOne(id);
        }
    }
}

function showComputerOne(id) {
    showLoad('computer-show');
    const request = {
        action: 'selectComputerById',
        data: id
    };
    client.send(JSON.stringify(request));
}

function showComputerOneResult(res) {
    hideLoad('computer-show');
    const {success, data} = res;
    if (!success) {
        alert('获取失败！');
        return;
    }
    way.set("computer.show", data);
    const {cpuUsage, diskUsage, memoryUsage, networkUsage} = data;
    if (window.computerStatusChart) {
        computerStatusChart.setOption({
            series: [{
                data: [{value: cpuUsage || 0, name: 'CPU占用率'}]
            }, {
                data: [{value: memoryUsage || 0, name: '内存占用率'}]
            }, {
                data: [{value: diskUsage || 0, name: '硬盘占用率'}]
            }, {
                data: [{value: networkUsage || 0, name: '带宽占用率'}]
            }]
        });
    }
}

function initTaskCountChart() {
    const data = [["2000-06-05", 116], ["2000-06-06", 129], ["2000-06-07", 135], ["2000-06-08", 86], ["2000-06-09", 73], ["2000-06-10", 85], ["2000-06-11", 73], ["2000-06-12", 68], ["2000-06-13", 92], ["2000-06-14", 130], ["2000-06-15", 245], ["2000-06-16", 139], ["2000-06-17", 115], ["2000-06-18", 111], ["2000-06-19", 309], ["2000-06-20", 206], ["2000-06-21", 137], ["2000-06-22", 128], ["2000-06-23", 85], ["2000-06-24", 94], ["2000-06-25", 71], ["2000-06-26", 106], ["2000-06-27", 84], ["2000-06-28", 93], ["2000-06-29", 85], ["2000-06-30", 73], ["2000-07-01", 83], ["2000-07-02", 125], ["2000-07-03", 107], ["2000-07-04", 82], ["2000-07-05", 44], ["2000-07-06", 72], ["2000-07-07", 106], ["2000-07-08", 107], ["2000-07-09", 66], ["2000-07-10", 91], ["2000-07-11", 92], ["2000-07-12", 113], ["2000-07-13", 107], ["2000-07-14", 131], ["2000-07-15", 111], ["2000-07-16", 64], ["2000-07-17", 69], ["2000-07-18", 88], ["2000-07-19", 77], ["2000-07-20", 83], ["2000-07-21", 111], ["2000-07-22", 57], ["2000-07-23", 55], ["2000-07-24", 60]];

    const dateList = data.map(function (item) {
        return item[0];
    });
    const valueList = data.map(function (item) {
        return item[1];
    });
    const taskCountChart = echarts.init(document.getElementById('task-count'));
    const taskCountOption = {
        title: [{
            left: 'center',
            text: '任务量统计',
            textStyle: {
                fontSize: 12,
                color: '#ffffff'
            },
        }, {
            top: '55%',
            left: 'center',
            text: '任务完成率',
            textStyle: {
                fontSize: 12,
                color: '#ffffff'
            },
        }],
        textStyle: {
            fontSize: 12,
            color: '#ffffff'
        },
        tooltip: {
            trigger: 'axis'
        },
        grid: [{
            top: '5%',
            bottom: '55%',
            right: '5%',
            left: '5%'
        }, {
            top: '60%',
            bottom: '5%',
            right: '5%',
            left: '5%'
        }],
        xAxis: [{
            data: dateList,
        }, {
            data: dateList,
            gridIndex: 1
        }],
        yAxis: [{
            splitLine: {show: false},
        }, {
            splitLine: {show: false},
            gridIndex: 1
        }],
        series: [{
            type: 'line',
            showSymbol: false,
            data: valueList,
            areaStyle: {},
            itemStyle: {
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                        offset: 0, color: 'red' // 0% 处的颜色
                    }, {
                        offset: 1, color: '#041960' // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                }
            },
        }, {
            type: 'line',
            showSymbol: false,
            data: valueList,
            xAxisIndex: 1,
            yAxisIndex: 1,
            areaStyle: {},
            itemStyle: {
                color: {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                        offset: 0, color: 'red' // 0% 处的颜色
                    }, {
                        offset: 1, color: '#041960' // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                }
            },
        }]
    };
    taskCountChart.setOption(taskCountOption);
}

function initComputerStatus() {
    const computerStatusChart = echarts.init(document.getElementById('computer-status'));
    const initOption = {
        type: 'gauge',
        radius: '50%',
        axisLine: {
            lineStyle: {
                width: '6'
            }
        },
        splitLine: {
            length: '6'
        },
        pointer: {
            width: '2',
            length: '60%'
        },
        axisTick: {
            show: false
        },
        title: {
            textStyle: {
                fontSize: 12,
                color: '#ffffff'
            },
            offsetCenter: ['0', '70%'],
        },
        detail: {
            formatter: function (value) {
                return value + '%';
            },
            fontSize: 14,
        },
    };
    const computerStatusOption = {
        tooltip: {
            formatter: "{a} <br/>{b} {c}% "
        },
        series: [
            {
                name: 'CPU',
                center: ['25%', '25%'],
                ...initOption,
                data: [{value: 40, name: 'CPU占用率'}]
            },
            {
                name: '内存',
                center: ['80%', '25%'],
                ...initOption,
                data: [{value: 14, name: '内存占用率'}]
            },
            {
                name: '硬盘',
                center: ['25%', '80%'],
                ...initOption,
                data: [{value: 5, name: '硬盘占用率'}]
            },
            {
                name: '带宽',
                center: ['80%', '80%'],
                ...initOption,
                data: [{value: 10, name: '带宽占用率'}]
            }
        ]
    };
    computerStatusChart.setOption(computerStatusOption);
    window.computerStatusChart = computerStatusChart;
}

function closeDialog() {
    const computerDialog = document.getElementById('computer-dialog');
    if (computerDialog) {
        computerDialog.style.display = 'none'
    }
    const taskDialog = document.getElementById('task-dialog');
    if (taskDialog) {
        taskDialog.style.display = 'none'
    }
}


function showLoad(id) {
    const x = document.getElementById(id);
    if (x && x.classList.contains("chart-done")) {
        x.classList.remove("chart-done");
    }
}

function hideLoad(id) {
    const x = document.getElementById(id);
    if (x) {
        x.classList.add("chart-done");
    }
}