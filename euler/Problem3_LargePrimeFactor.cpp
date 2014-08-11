#include<iostream>
using namespace std;

bool isPrime(unsigned long long num)
{
  cout << "Testing" << num << "for prime" << endl;

	unsigned long long search = 2;
  unsigned long long lastPrime = 1;

  while(search*search <= (num))
	{
		if(num % search == 0)
		{
			lastPrime = search;
		}
		search = search + 1;
	}	

	if(lastPrime == 1)
	{
		return true;
	}
	else
	{
		return false;
	}
}

unsigned long long getLargestPrimeFactor(unsigned long long num)
{
	unsigned long long search = num / 2;
	unsigned long long reduceBy = 1;
	while(search > 0)
	{
		if((num % search) == 0 && isPrime(search))
		{
			return search;
		}
    cout << search << endl;
		search = search - reduceBy;
	} 
}

int main(int argc, char** args)
{
	unsigned long long num;

	cout << "Enter a number" << endl;

	cin >> num;

	cout << "Largest prime factior is:" << getLargestPrimeFactor(num) << endl;
}
