#!/bin/bash

geth --datadir "./db" --networkid 5777 --rpc --rpcport "8545" --rpccorsdomain "*" --nodiscover --rpcapi="admin,db,eth,debug,miner,net,shh,txpool,personal,web3"
