class PrimeChecker 
{
  static isPrimeNumber(n) 
  {
    if (n < 2) return false;
    for (let i = 2; i * i <= n; i++) 
    {
      if (n % i === 0) 
      {
        return false;
      }
    }
    return true;
  }
}

function main() 
{
  console.log("Is 2 prime?", PrimeChecker.isPrimeNumber(2));   
}

main(); 

