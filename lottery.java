import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Evan Glazer - EvanGlazer.com
 * 
 */

enum winnings
    {
        // enum values
        THREE(10),
        FOUR(100),
        FIVE(10000),
        SIX(1000000);
        
        // constants value/list
        final int win;
        public static final winnings[] values = winnings.values();

        //winnings object
        winnings(int win)
        {
            this.win = win;
        }

        public int getWinnings()
        {
            return win;
        }
        //Check for winnings 
        public static int winningsCheck(int matches)
        {
            if (matches > 2)
            {
                return winnings.values[matches - 3].getWinnings();
            }
            else
            {
                return 0;
            }
        }
   

    public static class LotteryNum
    {
        int[] numbers = new int[6];

        public void setNumbers(int[] numbers) throws Exception
        {
            boolean first = true;
            //check for the correct count of numbers
            if (numbers.length == 6)
            {
                for (int i = 0; i < numbers.length; i++)
                {
                    //check if numbers are in range
                    if (first)
                    {
                        first = false;
                    }
                   
                this.numbers = numbers;
            }
          
        }
        }

        public void setNumbers(String numbers) throws Exception
        {
            int number;
            boolean first = true;

            // separate numbers \\s+
            String[] split = numbers.trim().split("\\s+");

            //check for the correct count of numbers
            if (split.length == 6)
            {
                for (int i = 0; i < split.length; i++)
                {
                    number = Integer.parseInt(split[i]);
                    this.numbers[i] = number;
                }
            }
         
            for (int i = 0; i < this.numbers.length; i++)
            {
                if (first)
                {
                    first = false;
                }
            }
        }

        public int[] getNumbers()
        {
            return numbers;
        }
    }

    // only the winnings numbers can check for matches
    // WinningNum will become a weak reference to map easier 
    public static class WinningNum extends LotteryNum
    {
        @Override
        public void setNumbers(int[] numbers) throws Exception
        {
            boolean first = true;
            super.setNumbers(numbers);
            for (int i = 0; i < this.numbers.length; i++)
            {
                if (first)
                {
                    first = false;
                }
            }
        }

        @Override
        public void setNumbers(String numbers) throws Exception
        {
            boolean first = true;
            super.setNumbers(numbers);
            for (int i = 0; i < this.numbers.length; i++)
            {
                if (first)
                {
                    first = false;
                }
            }
        }

        // check for the number of matches
        public int checkMatches(int[] numbers)
        {
            int matches = 0;

            // loop through unsorted ticket numbers
            for (int i : numbers)
            {
                // input is guaranteed to be sorted, so you can binary search
                // if return is positive, then the number was found
                if(Arrays.binarySearch(this.numbers, i) >= 0)
                {
                    matches++;
                }
            }
            return matches;
        }

        public int checkMatches(LotteryTicket ticket)
        {
            int matches = 0;

            // loop through unsorted ticket numbers
            for (int i : ticket.getNumbers())
            {
                // input is guaranteed to be sorted, so you can binary search
                // if return is positive, then the number was found
                if(Arrays.binarySearch(this.numbers, i) >= 0)
                {
                    matches++;
                }
            }
            return matches;
        }
    }

    public static class LotteryTicket extends LotteryNum
    {
        // Variables
        private int numbersMatched = 0;
        private char[] firstName;
        private char[] lastName;

        //Instaniates who is holds the winning tickets
        ArrayList<WinningNum> winningNumbers = null;

        public LotteryTicket(String lastName, String firstName)
        {
            this.firstName = firstName.toCharArray();
            this.lastName = lastName.toCharArray();
        }

        public int getNumbersMatched()
        {
            return this.numbersMatched;
        }

        // Checks ticket and returns winnings from method access
        public int checkTicket(WinningNum winningNumbers)
        {
            WinningNum temp = null;

            // check winning numbers
            if (this.winningNumbers == null || (temp = this.winningNumbers.get()) != null)
            {
                // return last case if null
                if (winningNumbers == null)
                {
                    return this.numbersMatched;
                }

                if (temp != winningNumbers)
                {
                    // set reference and check numbers
                    this.winningNumbers = new ArrayList<WinningNum>(winningNumbers);
                    this.numbersMatched = winningNumbers.checkMatches(this);
                }
            }
            else
            {
                //return 0 if null
                if (winningNumbers == null)
                {
                    return 0;
                }
                // set reference and check numbers
                this.winningNumbers = new ArrayList<WinningNum>(winningNumbers);
                this.numbersMatched = winningNumbers.checkMatches(this);
            }
            // return winnings
            return winnings.winningsCheck(this.numbersMatched);
        }

        @Override
        public String toString()
        {
            int win = this.checkTicket(null);
            return String.valueOf(this.firstName) + " " + String.valueOf(this.lastName)+ " matched " +
                    this.getNumbersMatched() + " numbers and won $" + winnings.winningsCheck(win) + ".";
        }
    }
    
public class lottery { }


    public static void main(String[] args) throws Exception
    {
        //variables
        Path filePath;
        WinningNum winningNumbers = new WinningNum();
        LotteryTicket[] tickets;

        // get input file and winning numbers
        System.out.println("Enter the path of the file:");
       
        
        try(Scanner in = new Scanner(System.in))
        {
            filePath = Paths.get(in.nextLine());
            if (!Files.exists(filePath))
            {
                throw new Exception("File does not exist.");
            }
            System.out.println("Enter the winning Lottery numbers: ");
            winningNumbers.setNumbers(new int[]{in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt(), in.nextInt()});
        }

        //run input file and winning numbers
        try(Scanner s1 = new Scanner(new BufferedInputStream(new FileInputStream(filePath.toFile()))))
        {

            tickets = new LotteryTicket[s1.nextInt()];
            s1.nextLine();
            for (int i = 0; i < tickets.length; i++)
            {
                tickets[i] = new LotteryTicket(s1.next(), s1.next());
                s1.nextLine();
                tickets[i].setNumbers(s1.nextLine());

                if(tickets[i].checkTicket(winningNumbers) != 0)
                {
                    System.out.println(tickets[i]);
                }
            }
        }
    }
}