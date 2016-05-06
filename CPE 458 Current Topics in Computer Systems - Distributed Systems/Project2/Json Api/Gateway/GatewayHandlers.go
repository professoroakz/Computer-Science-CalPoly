package main

import (
    "encoding/json"
    "fmt"
    "net/http"
    "io/ioutil"
    "io"
    "bytes"
    "time"
    // "strconv"

    "github.com/gorilla/mux"
)

// curl -H "Content-Type: application/json" -d '{"key":"1-2-06", "value":"bar"}' http://localhost:8080/

const dateFormat = "1-2-06"
const numNodes = 3
var nodes [numNodes]string
var indexToNodes map[int64][numNodes]int = make(map[int64][3]int)

func init(){
    nodes[0] = "localhost:8081"
    nodes[1] = "localhost:8082"
    nodes[2] = "localhost:8083"
    indexToNodes[0] = [numNodes]int{0, 1, 2}
    indexToNodes[1] = [numNodes]int{1, 2, 0}
    indexToNodes[2] = [numNodes]int{2, 0, 1}
}

// Takes a date and returns the responsible node
func GetNodeIndicies(date string) (nodeIndicies [numNodes]int, err error) {
    t, err := time.Parse(dateFormat, date)
    if err != nil {
        return
    }
    id := t.Unix() / (24*60*60)
    nodeIndicies = indexToNodes[id % numNodes]
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

    // Map key to list of 3 responsible nodes
    responsibleNodes, err := GetNodeIndicies(key)
    if err != nil {
        fmt.Println(w, "Invalid key")
        return
    }

    // Sends get to responsible node/s
    i := 0
    fmt.Println("GET http://" + nodes[responsibleNodes[i]] + "/" + key)
    resp, err := http.Get("http://" + nodes[responsibleNodes[i]] + "/" + key)
    for err != nil && i < 2 {
        i = i + 1
        fmt.Println("GET http://" + nodes[responsibleNodes[i]] + "/" + key)
        resp, err = http.Get("http://" + nodes[responsibleNodes[i]] + "/" + key)
    }

    // Found no responsible node
    if err != nil {
        fmt.Fprintln(w, "GET: No responsible node accessable")
        return
    }
    
    // Read response
    buf := new(bytes.Buffer)
    buf.ReadFrom(resp.Body)
    defer resp.Body.Close()
    fmt.Fprintln(w, buf.String())
}

func GatewayStockCreate(w http.ResponseWriter, r *http.Request) {
    var stock Stock

    body, _ := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))
    r.Body.Close()

    err := json.Unmarshal(body, &stock)
    if err != nil {
        fmt.Println("Error while reading Json")
        return
    }

    // Map key to list of 3 responsible nodes
    responsibleNodes, err := GetNodeIndicies(stock.Key)
    if err != nil {
        fmt.Println(w, "Invalid key")
        return
    }

    // Sends POST to responsible node
    jsonStr := bytes.NewBuffer([]byte(`{"key":"` + stock.Key + `","value":"` + stock.Value + `"}`))
    fmt.Println("POST", jsonStr)
    for i := 0; i < 3; i++ {
        fmt.Println("POST http://" + nodes[responsibleNodes[i]] + "/" + stock.Key)
        req, _ := http.NewRequest("POST", "http://" + nodes[responsibleNodes[i]], jsonStr)
        req.Header.Set("Content-Type", "application/json")
        client := &http.Client{}
        _, err := client.Do(req)
        if err != nil {
            fmt.Fprintln(w, "POST: Cannot access " + nodes[responsibleNodes[i]])
            return
        }
    }
}