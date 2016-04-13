#include <stdio.h>
#include <math.h>

int SQR(double num) {
    return (int)sqrt(num);
}

int XsquaredPlusY(int X, int Y, int(*f)(double)) {
    return Y + (int)f(X);
}

int main(int argc, char const *argv[])
{
    printf("%d\n", XsquaredPlusY(25, 5, &SQR));
    return 0;
}
