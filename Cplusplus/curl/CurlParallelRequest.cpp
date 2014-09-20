#include <stdio.h>
#include <unistd.h>
#include <pthread.h>
#include <curl/curl.h>
#include <boost/thread/tss.hpp>
#include <iostream>

using namespace std;

class HTTPHandle
{
  private:
    CURL* curl;

  public:

    HTTPHandle()
    {
      cout << "Creating curl easy handle" << endl;  
      curl = curl_easy_init();
    }
 
    CURL* getHandle()
    {
       return curl;
    }

    ~HTTPHandle()
    {
      cout << "Cleaning up easy handle" << endl;
      curl_easy_cleanup(curl);
    }
};

void destroyHttpHandle(HTTPHandle* ptr)
{
  delete ptr;
}
void makeRequest()
{
  
  static boost::thread_specific_ptr<HTTPHandle> instance(destroyHttpHandle);
  // Create new HTTPHandle object if not already created one for this thread
  if(! instance.get())
  {
    instance.reset(new HTTPHandle);
  }

  CURL* curl = instance->getHandle();

  CURLcode res;

  if(curl) {
    curl_easy_setopt(curl, CURLOPT_URL, "http://10.223.3.131");
    // example.com is redirected, so we tell libcurl to follow redirection
    curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);

    // Perform the request, res will get the return code
    res = curl_easy_perform(curl);
    // Check for errors
    if(res != CURLE_OK)
      cerr << "curl_easy_perform() failed:" << curl_easy_strerror(res) << endl;
  }
} 

// Starting point of thread. Makes two HTTP request
void* thread_func(void* arg)
{
  for(int i = 1; i <= 2; i++)
  {
    makeRequest();
  }
 
  pthread_exit(NULL); 
}

int main(void)
{
  pthread_t thread1, thread2;

  cout << "Global curl init." << endl;
  //initialize libcurl
  curl_global_init(CURL_GLOBAL_ALL);


  //Start two threads.

  pthread_create(&thread1, NULL, thread_func, NULL);

  pthread_create(&thread2, NULL, thread_func, NULL);

  // Wait for two threads to finish 
  pthread_join(thread1, NULL);
  pthread_join(thread2, NULL);

  // Two threads completed. Each would have made two HTTP requests. No. oc connection to 10.223.3.131 would be only two.
  // Since each thread creates only one curl handle. Check this using 'netstat -nap | grep 10.223.3.131'
  
  cout << "Check output of 'netstat -nap | grep 10.223.3.131'" << endl;
  
  sleep(5);  

  cout << "Global curl cleanup." << endl;
  // global libcurl cleanup
  curl_global_cleanup();

}
