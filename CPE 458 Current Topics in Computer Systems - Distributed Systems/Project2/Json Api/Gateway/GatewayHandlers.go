package main

import (
    "encoding/json"
    "fmt"
    "net/http"
    "io/ioutil"
    "io"
    "bytes"
    "time"
    "strconv"

    "github.com/gorilla/mux"
)

// curl -H "Content-Type: application/json" -d '{"key":"1-2-06", "value":"bar"}' http://localhost:8080/

const dateFormat = "1-2-06"
const numNodes = 2

// Takes a date and returns the responsible node
func GetNode(date string) (node string, err error) {
    t, err := time.Parse(dateFormat, date)
    if err != nil {
        return
    }
    id := t.Unix() / (24*60*60)
    node = strconv.FormatInt(id % numNodes + 1, 10) // 1 based index
    err = nil
    return
}

func Index(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Welcome!")
    fmt.Fprintln(w, "GET on /{key} returns value")
    fmt.Fprintln(w, "POST on / with {\"key\":\"{key}\", \"value\":\"{value}\"} puts new key-value pair")
}

// Get handler
func GatewayStockShow(w http.ResponseWriter, r *http.Request){
    vars := mux.Vars(r)
    key := vars["key"]
    fmt.Println("KEY: " + key)

    node, err := GetNode(key)
    if err != nil {
        fmt.Println(w, "Invalid key")
        return
    }

    // Sends get to responsible node
    fmt.Println("GET http://localhost:808" + node + "/" + key)
    resp, err := http.Get("http://localhost:808" + node + "/" + key)
    if err != nil {
        fmt.Fprintln(w, "GET: Node " + node + " not available")
        return
    }
    
    buf := new(bytes.Buffer)
    buf.ReadFrom(resp.Body)
    defer resp.Body.Close()
    fmt.Fprintln(w, buf.String())
}

func GatewayStockCreate(w http.ResponseWriter, r *http.Request) {
    var stock Stock

    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    if err != nil {
        panic(err)
    }
    if err := r.Body.Close(); err != nil {
        panic(err)
    }
    err = json.Unmarshal(body, &stock)
    // if err != nil {
    //     return
    // }

    node, err := GetNode(stock.Key)
    if err != nil {
        fmt.Println(w, "Invalid key")
        return
    }

    // Sends POST to responsible node
    var jsonStr = []byte(`{"key":"` + stock.Key + `","value":"` + stock.Value + `"}`)
    req, err := http.NewRequest("POST", "http://localhost:808" + node, bytes.NewBuffer(jsonStr))
    req.Header.Set("Content-Type", "application/json")

    client := &http.Client{}
    resp, err := client.Do(req)

    if err != nil {
        fmt.Sprintln("POST: Node " + node + " not available")
        return
    }
    
    buf := new(bytes.Buffer)
    buf.ReadFrom(resp.Body)
    defer resp.Body.Close()
    fmt.Fprintln(w, buf.String())
}