#!/bin/bash

rm -R db
geth --datadir=./db account new
# Copy/paste private key
geth --datadir=./db init Genesis.json
geth --datadir "./db" --networkid 5777 --rpc --rpcport "8545" --rpccorsdomain "*" --nodiscover --rpcapi="admin,db,eth,debug,miner,net,shh,txpool,personal,web3"

exit 0
