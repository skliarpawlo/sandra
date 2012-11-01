using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Diagnostics;

namespace ProcessTry
{
    class Program
    {
        /// <summary>
        /// утилита для запуска программы с заданным лимитом времени
        /// </summary>
        /// <param name="args"> 0 - имя программы, 1 - тайм лимит </param>
        static void Main( string[] args )
        {

            if (args.Length < 2) 
            {
                System.Console.WriteLine( "no enought params." );
                return;
            }

            try
            {
                Process p = new Process();

                p.StartInfo.FileName = args[0];

                p.StartInfo.RedirectStandardError = true;
                p.StartInfo.RedirectStandardInput = true;
                p.StartInfo.RedirectStandardOutput = true;

                p.StartInfo.UseShellExecute = false;

                p.Start();

                p.WaitForExit(int.Parse(args[1]));

                if (!p.HasExited)
                {
                    p.Kill();
                    System.Console.WriteLine("timelimit");
                }
                else
                {
                    if (p.ExitCode == 0)
                    {
                        System.Console.WriteLine("okay");
                    }
                    else
                    {
                        System.Console.WriteLine("runtime");
                    }
                }

                TimeSpan time = p.ExitTime.Subtract(p.StartTime);

                System.Console.WriteLine(time.Seconds * 1000 + time.Milliseconds);

            }
            catch ( Exception e ) 
            {
                System.Console.WriteLine("error");
                System.Console.WriteLine("0");
            }
        }
    }
}
