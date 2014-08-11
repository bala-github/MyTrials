#include <iostream>

using namespace std;

int main(int argc, char** argv)
{
  int n = 0;
  int x = 0, y = 0;
  int sum = 0;

  while(n < 1000)
  {
    
    if(0 == n % 3)
    {
      // point to the next divisible of three.
      x = x + 3;
    }

    if(0 == n % 5)
    {
      // point to the next divisible of five.
      y = y + 5;
    }

  
    sum = sum + n;

    cout << "n:" << n << " x:" << x << " y:" << y << " sum:" << sum << endl;   
   
    n =  x < y ? x : y;   

  
  }

  cout << sum << endl;
}

