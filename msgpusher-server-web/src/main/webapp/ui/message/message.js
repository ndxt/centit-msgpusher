(function defineModule (global, factory) {
    if (typeof exports === 'object' && exports && typeof exports.nodeName !== 'string') {
        factory(exports); // CommonJS
    } else if (typeof define === 'function' && define.amd) {
        define(['exports'], factory); // AMD
    } else {
        global.Message = {};
        factory(global.Message); // script, wsh, asp
    }
})(this, function messageFactory (message) {

    // 默认属性
    var DEFAULT_CONFIG = {
        contextPath: undefined,     // 如果需要配置，必须为全地址 http://localhost:8080/test
        createUrl: '/pusher/{osId}/{userCode}'  //
    };

    message.configure = configure;
    message.create = create;

    /**
     * 配置全局属性
     * @param config
     */
    function configure (config) {
        if (!config) {
            return
        }

        DEFAULT_CONFIG = _extend(DEFAULT_CONFIG, config);
    }

    /**
     * 创建WebSocket链接
     * @param osId
     * @param userCode
     * @param config
     */
    function create (osId, userCode, config) {

        var options = _extend(DEFAULT_CONFIG, config);

        if (typeof WebSocket !== 'function') {
            throw Error('浏览器不支持WebSocket')
        }

        var wsHost = options.contextPath || _getContextPath();
        wsHost += options.createUrl;
        wsHost = wsHost.replace(/^http/, 'ws');
        wsHost = _replacePlaceholder(wsHost, {
            osId: osId,
            userCode: userCode
        });

        var socket = new WebSocket(wsHost);

        return new WebSocketMessage(socket, _extend(options, {
            wsHost: wsHost,
            osId: osId,
            userCode: userCode
        }))
    }

    /**
     * WebScoket消息类
     * @param socket
     * @param config
     * @constructor
     */
    function WebSocketMessage (socket, config) {
        this.socket = socket;
        this.config = config;
        this.methods = {
            open: function () {
                console.log('WebSocket is opened.')
            },
            message: function (res) {
                console.log('message: ', _parseWebSocketMessage(res))
            },
            close: function () {
                console.log('WebSocket is closed.')
            }
        };

        // 打开
        socket.onopen = function () {
            (this.methods.open || _noop).apply(this, arguments)
        }.bind(this);

        // 有信息
        socket.onmessage = function (res) {
            var data = _parseWebSocketMessage(res);
            var type = data.msgType;


            (this.methods.message || _noop).apply(this, [data]);

            // 指定消息类型
            if (type && typeof this.methods[type] === 'function') {
                this.methods[type].apply(this, [data])
            }
        }.bind(this);

        // 结束
        socket.onclose = function () {
            (this.methods.close || _noop).apply(this, arguments)
        }.bind(this)
    }

    /**
     * 添加事件类型
     * @param method
     * @param fn
     */
    WebSocketMessage.prototype.on = function (method, fn) {
        var canNotNamedMethod = ['close', 'open', 'message'];
        if (canNotNamedMethod.indexOf(method) > -1) {
            throw Error('方法名不能为['+ canNotNamedMethod.join(',') +']');
        }

        this.methods[method] = fn;
    };

    /**
     * 取消事件类型
     * @param method
     */
    WebSocketMessage.prototype.off = function (method) {
        var canNotNamedMethod = ['close', 'open', 'message'];
        if (canNotNamedMethod.indexOf(method) > -1) {
            throw Error('方法名不能为['+ canNotNamedMethod.join(',') +']');
        }

        delete this.methods[method];
    };

    /**
     * 发送消息
     * @param msg
     */
    WebSocketMessage.prototype.send = function (msg) {
        if (typeof msg === 'string') {
            this.socket.send(msg)
        } else {
            this.socket.send(JSON.stringify(msg))
        }
    };

    /**
     * 工具函数：解析WebSocket消息
     * @param res
     * @returns {*}
     * @private
     */
    function _parseWebSocketMessage (res) {
        var msg;

        try {
            msg = JSON.parse(res.data);
        }
        catch(e) {
            msg = res.data;
        }

        return msg;
    }

    function _noop () {}


    /**
     * 工具函数：替换字符串中的占位符
     * @param str
     * @param params
     * @returns {*}
     * @private
     */
    function _replacePlaceholder (str, params) {
        for (var name in params) {
            if (!params.hasOwnProperty(name)) {
                continue;
            }

            var value = params[name];
            var regExp = new RegExp('\{'+ name +'\}');
            str = str.replace(regExp, value)
        }

        return str;
    }

    /**
     * 工具函数：扩展对象
     * @param target
     * @param source
     * @returns {{}}
     * @private
     */
    function _extend (target, source) {
        var result = {};
        [target, source].forEach(function (node) {
            for (var name in node) {
                if (!node.hasOwnProperty(name)) {
                    continue;
                }

                var value = node[name];
                (value !== undefined && value !== null) && (result[name] = value)
            }
        });

        return result;
    }

    /**
     * 工具函数：获取当前contentPath
     * @returns {*}
     * @private
     */
    function _getContextPath() {
        var match = location.href.match(/^(http:\/\/.*?\/.*?)\//);

        if (match && match[1]) {
            return match[1].replace(/^http/, 'ws')
        }
    }

    return message;
});