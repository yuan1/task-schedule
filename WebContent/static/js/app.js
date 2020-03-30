window.onload = function () {
    this.initTaskCountChart();
    this.initComputerStatus();
    const client = new WebsocketClient('/endpoint', (res) => {
        if (res.action === 'onopen') {
            this.loadComputer();
        }
        if (res.action === 'onmessage') {
            const body = JSON.parse(res.body);
            const {action} = body;
            if (action === 'loadComputer') {
                this.loadComputerResult(body)
            }
        }
    });
    client.connect();
    window.client = client;
};

function loadComputer() {
    const x = document.getElementById('computer-manager');
    if (x.classList.contains("chart-done")) {
        x.classList.remove("chart-done");
    }
    const request = {
        action: 'loadComputer'
    };
    client.send(JSON.stringify(request));
}

function loadComputerResult(res) {
    console.log('loadComputer', res);
    const {data, success, msg} = res;
    if (!success) {
        console.error(msg);
        return;
    }
    way.set("computer.list", data);
    const x = document.getElementById('computer-manager');
    x.classList.add("chart-done");
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
    }
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
    computerStatusChart.setOption(computerStatusOption)
}
