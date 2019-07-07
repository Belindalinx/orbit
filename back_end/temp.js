var keythereum = require("keythereum");
var datadir = "/home/thibaut/TEMP/Blockchain/eth-chain/db";
var address= "0x81a774b8947679ae8deaec6492b27a134d1e85a9";
const password = "azerty";

var keyObject = keythereum.importFromFile(address, datadir);
var privateKey = keythereum.recover(password, keyObject);
console.log(privateKey.toString('hex'));
