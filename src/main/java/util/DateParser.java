package main.java.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class DateParser
{
    public static LocalDate parseDate(String date)
    {
        /* DATE FORMAT: By patterns. */
        String[] patterns = {
                "dd/MM/yyyy",
                "d/MM/yyyy",
                "dd/M/yyyy",
                "d/M/yyyy",
                "dd-MM-yyyy",
                "d-MM-yyyy",
                "dd-M-yyyy",
                "d-M-yyyy",
                "yyyy/MM/dd",
                "yyyy/M/dd",
                "yyyy/MM/d",
                "yyyy/M/d",
                "yyyy-MM-dd",
                "yyyy-M-dd",
                "yyyy-MM-d",
                "yyyy-M-d",
                "dd MMM yyyy",
                "d MMM yyyy",
                "dd/MM/yy",
                "d/MM/yy",
                "dd/M/yy",
                "d/M/yy",
                "dd-MM-yy",
                "d-MM-yy",
                "dd-M-yy",
                "d-M-yy"
                // Dangerous territory below. double-digit years in the front is ambiguous and can be misinterpreted!
//                "yy/MM/dd",
//                "yy/M/dd",
//                "yy/MM/d",
//                "yy/M/d",
//                "yy-MM-dd",
//                "yy-M-dd",
//                "yy-MM-d",
//                "yy-M-d"
        };

        for (String pattern : patterns)
        {
            try
            {
                DateTimeFormatter formatter  = DateTimeFormatter.ofPattern(pattern);
                LocalDate         parsedDate = LocalDate.parse(date, formatter);
                
                /*
                Supports yy (two-digit year) and yyyy (four-digit year).
                Year adjustment for yy:
                
                    00-49 → 2000-2049
                    50-99 → 1950-1999
                
                Uses ChronoField.YEAR to fix two-digit years after parsing.
                 */
                if (pattern.contains("yy"))
                {
                    int year = parsedDate.getYear();
                    if (year < 50)
                    {
                        parsedDate = parsedDate.with(ChronoField.YEAR, 2000 + year);
                    }
                    else if (year < 100)
                    {
                        parsedDate = parsedDate.with(ChronoField.YEAR, 1900 + year);
                    }
                }

                return parsedDate;
            }
            catch (DateTimeParseException ignored) {}
        }

        /* DATE FORMAT: Excel 1900 base. */
        try
        {
            int       dateNumber = Integer.parseInt(date);
            LocalDate baseDate   = LocalDate.of(1900, 1, 1);

            return baseDate.plusDays(dateNumber - 1);
        }
        catch (Exception ignored) {}

        /* DID NOT FIND ANY COMPATIBLE DATES */
        ConsoleMessage.error(new Exception(), "INVALID DATE FORMAT!");
        return null; // Return null or handle appropriately
    }
}
