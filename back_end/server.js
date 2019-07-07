var Web3 = require('web3');
var sqlite3 = require('sqlite3').verbose();
var path = require('path');
var dbPath = path.resolve(__dirname, 'orbit.db')
var db = new sqlite3.Database(dbPath);
var express = require('express');
var md5 = require("md5");
var bodyParser = require("body-parser");

var ctx = express();

var human_standard_token_abi = [
    {
        "constant": true,
        "inputs": [],
        "name": "name",
        "outputs": [
            {
                "name": "",
                "type": "string"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": true,
        "inputs": [],
        "name": "symbol",
        "outputs": [
            {
                "name": "",
                "type": "string"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": true,
        "inputs": [],
        "name": "decimals",
        "outputs": [
            {
                "name": "",
                "type": "uint8"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": true,
        "inputs": [],
        "name": "totalSupply",
        "outputs": [
            {
                "name": "",
                "type": "uint256"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": true,
        "inputs": [
            {
                "name": "_owner",
                "type": "address"
            }
        ],
        "name": "balanceOf",
        "outputs": [
            {
                "name": "balance",
                "type": "uint256"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": false,
        "inputs": [
            {
                "name": "_to",
                "type": "address"
            },
            {
                "name": "_value",
                "type": "uint256"
            }
        ],
        "name": "transfer",
        "outputs": [
            {
                "name": "success",
                "type": "bool"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": false,
        "inputs": [
            {
                "name": "_from",
                "type": "address"
            },
            {
                "name": "_to",
                "type": "address"
            },
            {
                "name": "_value",
                "type": "uint256"
            }
        ],
        "name": "transferFrom",
        "outputs": [
            {
                "name": "success",
                "type": "bool"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": false,
        "inputs": [
            {
                "name": "_spender",
                "type": "address"
            },
            {
                "name": "_value",
                "type": "uint256"
            }
        ],
        "name": "approve",
        "outputs": [
            {
                "name": "success",
                "type": "bool"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": true,
        "inputs": [
            {
                "name": "_owner",
                "type": "address"
            },
            {
                "name": "_spender",
                "type": "address"
            }
        ],
        "name": "allowance",
        "outputs": [
            {
                "name": "remaining",
                "type": "uint256"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "anonymous": false,
        "inputs": [
            {
                "indexed": true,
                "name": "_from",
                "type": "address"
            },
            {
                "indexed": true,
                "name": "_to",
                "type": "address"
            },
            {
                "indexed": false,
                "name": "_value",
                "type": "uint256"
            }
        ],
        "name": "Transfer",
        "type": "event"
    },
    {
        "anonymous": false,
        "inputs": [
            {
                "indexed": true,
                "name": "_owner",
                "type": "address"
            },
            {
                "indexed": true,
                "name": "_spender",
                "type": "address"
            },
            {
                "indexed": false,
                "name": "_value",
                "type": "uint256"
            }
        ],
        "name": "Approval",
        "type": "event"
    },
    {
        "inputs": [
            {
                "name": "_initialAmount",
                "type": "uint256"
            },
            {
                "name": "_tokenName",
                "type": "string"
            },
            {
                "name": "_decimalUnits",
                "type": "uint8"
            },
            {
                "name": "_tokenSymbol",
                "type": "string"
            }
        ],
        "payable": false,
        "type": "constructor"
    },
    {
        "constant": false,
        "inputs": [
            {
                "name": "_spender",
                "type": "address"
            },
            {
                "name": "_value",
                "type": "uint256"
            },
            {
                "name": "_extraData",
                "type": "bytes"
            }
        ],
        "name": "approveAndCall",
        "outputs": [
            {
                "name": "success",
                "type": "bool"
            }
        ],
        "payable": false,
        "type": "function"
    },
    {
        "constant": true,
        "inputs": [],
        "name": "version",
        "outputs": [
            {
                "name": "",
                "type": "string"
            }
        ],
        "payable": false,
        "type": "function"
    }
];

var web3 = new Web3(new Web3.providers.HttpProvider("http://127.0.0.1:8545"));
web3.eth.defaultAccount = web3.eth.accounts[0];
address = "0x81a774b8947679ae8deaec6492b27a134d1e85a9"
address2 = "0x7d8ebe5087774332496ef9259a7c5518041abcbf"
contractAddress = "0xCbF64630fD5B1D1E033502D842C82a083d6a013D"
contractABI = human_standard_token_abi

var tokenContract = web3.eth.Contract(contractABI, contractAddress, { from: address });

ctx.use(bodyParser.urlencoded({ extended: false }));
ctx.use(bodyParser.json());


// GET

ctx.get('/users', function(req, res){
    db.all("SELECT * FROM users", function(err, row){
        res.json(row);
    });
});

ctx.get('/user/:id', function(req, res){
    db.get("SELECT * FROM users WHERE user_id = ?", req.params.id, function(err, row){
        res.json(row);
    });
});

ctx.get('/transactions', function(req, res){
    db.all("SELECT * FROM transactions", function(err, row){
        res.json(row);
    });
});

ctx.get('/transaction/:id', function(req, res){
    db.get("SELECT * FROM transactions where tx_id = ?", req.params.id, function(err, row){
        res.json(row);
    });
});

ctx.get('/hotspots', function(req, res){
    db.all("SELECT * FROM hs_map", function(err, row){
        res.json(row);
    });
});

ctx.get('/hotspot/:id', function(req, res){
    db.get("SELECT * FROM hs_map where hs_id = ?", req.params.id, function(err, row){
        res.json(row);
    });
});

// NEWS

ctx.post('/user/new', function(req, res){
    var data = {
      user_wlt_addr: req.body.user_wlt_addr,
      user_type: req.body.user_type,
      user_amt: req.body.user_amt,
      user_name: req.body.user_name,
      user_pwd: req.body.user_pwd,
      user_email: req.body.user_email,
      user_loc_lat: req.body.user_loc_lat,
      user_loc_lng: req.body.user_loc_lng,
      user_legal: req.body.user_legal
    }
    db.run("INSERT INTO users (user_wlt_addr, user_type, user_amt, user_name, user_pwd, user_email, user_loc_lat, user_loc_lng, user_legal) VALUES (?,?,?,?,?,?,?,?,?)", [data.user_wlt_addr, data.user_type, data.user_amt, data.user_name, data.user_pwd, data.user_email, data.user_loc_lat, data.user_loc_lng, data.user_legal], function(err, row){
      if (err){
        res.status(400).json({"error": res.message})
        console.log(res.message);
        return;
      }
      res.json({
        data: data
      });
    });
});

ctx.post('/transaction/new', function(req, res){
  var data = {
    tx_receiver: req.body.tx_receiver,
    tx_sender: req.body.tx_sender,
    tx_amt: req.body.tx_amt,
    tx_type: req.body.tx_type,
    tx_time: req.body.tx_time
  }
  console.log("Received new transaction\r");
  console.log("Writing it on the databasei\r");
  db.run("INSERT INTO transactions (tx_receiver, tx_sender, tx_amt, tx_type, tx_time) VALUES (?,?,?,?,?)", [data.tx_receiver, data.tx_sender, data.tx_amt, data.tx_type, data.tx_time], function(err, row){
    if (err){
      res.status(400).json({"error": res.message})
      return;
    }
    console.log("OK");
    console.log("Sending transaction to the Smart Contract");

    // ENVOI DANS BLOCKCHAIN
    let minABI = [
    {
        "constant": false,
        "inputs": [
            {
                "name": "_to",
                "type": "address"
            },
            {
                "name": "_value",
                "type": "uint256"
            }
        ],
        "name": "transfer",
        "outputs": [
            {
                "name": "success",
                "type": "bool"
            }
        ],
        "payable": false,
        "stateMutability": "nonpayable",
        "type": "function"
    }
  ];

    let contract = new web3.eth.Contract(minABI, contractAddress);

    contract.methods.transfer(data.tx_receiver, data.tx_amt).send({
            from: data.tx_sender
    });
    console.log("OK");

    res.json({
      data: data,
      changes: this.changes
    });
  });
});

ctx.post('/hotspot/new', function(req, res){
  var data = {
    hs_mac: req.body.hs_mac,
    hs_SSID: req.body.hs_SSID,
    hs_pwd: req.body.hs_pwd,
    hs_type: req.body.hs_type,
    hs_down_Mbps: req.body.hs_down_Mbps,
    hs_up_Mbps: req.body.hs_up_Mbps,
    hs_ping_ms: req.body.hs_ping_ms,
    hs_info: req.body.hs_info,
    hs_owner: req.body.hs_owner,
    hs_loc_lat: req.body.hs_loc_lat,
    hs_loc_lng: req.body.hs_loc_lng
  }
  db.run("INSERT INTO hs_map (hs_mac, hs_SSID, hs_pwd, hs_type, hs_down_Mbps, hs_up_Mbps, hs_ping_ms, hs_info, hs_loc_lat, hs_loc_lng, hs_owner) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
  [data.hs_mac, data.hs_SSID, data.hs_pwd, data.hs_type, data.hs_down_Mbps, data.hs_up_Mbps, data.hs_ping_ms, data.hs_info, data.hs_owner, data.hs_loc_lat, data.hs_loc_lng], function(err, row){
    if (err){
      res.status(400).json({"error": res.message})
      return;
    }
    res.json({
      data: data,
      changes: this.changes
    });
  });
});


// PATCH

ctx.post('/user/:id', function(req, res){
    var data = {
      user_wlt_addr: req.body.user_wlt_addr,
      user_type: req.body.user_type,
      user_amt: req.body.user_amt,
      user_name: req.body.user_name,
      user_pwd: req.body.user_pwd ? md5(req.body.user_pwd) : null,
      user_email: req.body.user_email,
      user_loc_lat: req.body.user_loc_lat,
      user_loc_lng: req.body.user_loc_lng,
      user_legal: req.body.user_legal
    }
    db.run("UPDATE users SET user_wlt_addr = COALESCE(?, user_wlt_addr), user_type = COALESCE(?, user_type), user_amt = COALESCE(?, user_amt), user_name = COALESCE(?, user_name), user_pwd = COALESCE(?, user_pwd), user_email = COALESCE(?, user_email), user_loc_lat = COALESCE(?, user_loc_lat), user_loc_lng = COALESCE(?, user_loc_lng), user_legal = COALESCE(?, user_legal) WHERE user_id = ?", [data.user_wlt_addr, data.user_type, data.user_amt, data.user_name, data.user_pwd, data.user_email, data.user_loc_lat, data.user_loc_lng, data.user_legal, req.params.id], function(err, row){
      if (err){
        res.status(400).json({"error": res.message})
        return;
      }
      res.json({
        data: data,
        changes: this.changes
      });
    });
});

ctx.post('/transaction/:id', function(req, res){
  var data = {
    tx_receiver: req.body.user_wlt_addr,
    tx_sender: req.body.user_type,
    tx_amt: req.body.user_amt,
    tx_type: req.body.user_name,
    tx_time: req.body.user_pwd
  }
  db.run("UPDATE transactions SET tx_receiver = COALESCE(?, tx_receiver), tx_sender = COALESCE(?, tx_sender), tx_amt = COALESCE(?, tx_amt), tx_type = COALESCE(?, tx_type), tx_time = COALESCE(?, tx_time), user_email = COALESCE(?, user_email), user_loc_lat = COALESCE(?, user_loc_lat), user_loc_lng = COALESCE(?, user_loc_lng), user_legal = COALESCE(?, user_legal) WHERE user_id = ?", [data.tx_receiver, data.tx_sender, data.tx_amt, data.tx_type, data.tx_time, req.params.id], function(err, row){
    if (err){
      res.status(400).json({"error": res.message})
      return;
    }
    res.json({
      data: data,
      changes: this.changes
    });
  });
});

ctx.post('/hotspot/:id', function(req, res){
  var data = {
    hs_mac: req.body.hs_mac,
    hs_SSID: req.body.hs_SSID,
    hs_pwd: req.body.hs_pwd,
    hs_type: req.body.hs_type,
    hs_down_Mbps: req.body.hs_down_Mbps,
    hs_up_Mbps: req.body.hs_up_Mbps,
    hs_ping_ms: req.body.hs_ping_ms,
    hs_info: req.body.hs_info,
    hs_owner: req.body.hs_owner,
    hs_loc_lat: req.body.hs_loc_lat,
    hs_loc_lng: req.body.hs_loc_lng
  }
  db.run("UPDATE hs_map SET hs_mac = COALESCE(?, hs_mac), hs_SSID = COALESCE(?, hs_SSID), hs_pwd = COALESCE(?, hs_pwd), hs_type = COALESCE(?, hs_type), hs_down_Mbps = COALESCE(?, hs_down_Mbps), hs_up_Mbps = COALESCE(?, hs_up_Mbps), hs_ping_ms = COALESCE(?, hs_ping_ms), hs_info = COALESCE(?, hs_info), hs_loc_lat = COALESCE(?, hs_loc_lat), hs_loc_lng = COALESCE(?, hs_loc_lng), hs_owner = COALESCE(?, hs_owner) WHERE hs_id = ?",
  [data.hs_mac, data.hs_SSID, data.hs_pwd, data.hs_type, data.hs_down_Mbps, data.hs_up_Mbps, data.hs_ping_ms, data.hs_info, data.hs_owner, data.hs_loc_lat, data.hs_loc_lng, req.params.id], function(err, row){
    if (err){
      res.status(400).json({"error": res.message})
      return;
    }
    res.json({
      data: data,
      changes: this.changes
    });
  });
});


ctx.listen(3000);


console.log("");
console.log(".______        ___       ______  __  ___         _______ .__   __.  _______");
console.log("|   _  \\      /   \\     /      ||  |/  /        |   ____||  \\ |  | |       \\ ");
console.log("|  |_)  |    /  ^  \\   |  ,----'|  '  /   ______|  |__   |   \\|  | |  .--.  | ");
console.log("|   _  <    /  /_\\  \\  |  |     |    <   |______|   __|  |  . `  | |  |  |  | ");
console.log("|  |_)  |  /  _____  \\ |  `----.|  .  \\         |  |____ |  |\\   | |  '--'  | ");
console.log("|______/  /__/     \\__\\ \\______||__|\\__\\        |_______||__| \\__| |_______/ ");

console.log("");


console.log("Listening on port 3000");

//tokenContract.balanceOf(address).toNumber()
