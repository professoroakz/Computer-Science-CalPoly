#include <iterator>
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>

std::vector<std::string> getNextLineAndSplitIntoTokens(std::istream& str)
{
    std::vector<std::string>   result;
    std::string                line;
    std::getline(str,line);

    std::stringstream          lineStream(line);
    std::string                cell;

    while(std::getline(lineStream,cell,','))
    {
        result.push_back(cell);
    }
    return result;
}


int main(int argc, char const *argv[])
{
    std::ifstream   file("aapl.csv");
    std::vector<std::string> v = getNextLineAndSplitIntoTokens(file);

    for(int i = 0; i < v.size(); i++) {
        std::vector<std::string> v = getNextLineAndSplitIntoTokens(file);
        std::cout << v.front() + '\n';
    }

    return 0;
}