package main

type Stock struct {
    Key       string   `json:"key"`
    Value	  string   `json:"value"`
}

type Stocks []Stock