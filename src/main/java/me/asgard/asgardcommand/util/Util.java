package me.asgard.asgardcommand.util;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static void sendMsg (CommandSender commandSender, String msg) {
		commandSender.sendMessage(colored(msg));
	}

	public static String colored(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String convertStr (String[] args, int start) {
		StringBuffer sb = new StringBuffer();
		for (int i = start; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public static String convertStr (String[] args, int start, int end) {
		if (end > args.length) end = args.length;
		StringBuffer sb = new StringBuffer();
		for (int i = start; i < end; i++) {
			sb.append(args[i]);
			if (i < end - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(convertStr("s 1 2 3 4".split(" "), 2));
	}
//
//	public static void main(String[] args) {
//		try {
//			System.out.println(System.currentTimeMillis());
//			System.out.println(parseDateDiff("5000ms", true));
//			new Thread(() -> {
//				String [] ss  = {"a","bb","a","bb"};
//				for (String s : ss) {
//					if (s.equals("a"))
//						try {
//							Thread.sleep(2000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					System.out.println(s);
//				}
//			}).start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	  public static long parseDateDiff(String time, boolean future)
			    throws Exception
			  {
			    Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*s[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:ms[a-z]*)?)", 2);
			    Matcher m = timePattern.matcher(time);
			    int years = 0;
			    int months = 0;
			    int weeks = 0;
			    int days = 0;
			    int hours = 0;
			    int minutes = 0;
			    int seconds = 0;
			    int ms = 0;
			    boolean found = false;
			    while (m.find()) {
			      if ((m.group() != null) && (!m.group().isEmpty()))
			      {
			        for (int i = 0; i < m.groupCount(); i++) {
			          if ((m.group(i) != null) && (!m.group(i).isEmpty()))
			          {
			            found = true;
			            break;
			          }
			        }
			        if (found)
			        {
			          if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
			            years = Integer.parseInt(m.group(1));
			          }
			          if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
			            months = Integer.parseInt(m.group(2));
			          }
			          if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
			            weeks = Integer.parseInt(m.group(3));
			          }
			          if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
			            days = Integer.parseInt(m.group(4));
			          }
			          if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
			            hours = Integer.parseInt(m.group(5));
			          }
			          if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
			            minutes = Integer.parseInt(m.group(6));
			          }
			          if ((m.group(7) != null) && (!m.group(7).isEmpty())) {
			            seconds = Integer.parseInt(m.group(7));
			          }
			          if ((m.group(8) != null) && (!m.group(8).isEmpty())) {
				          ms = Integer.parseInt(m.group(8));
				      }
			        }
			      }
			    }
			    if (!found) {
			      throw new Exception("时间格式错误");
			    }
			    Calendar c = new GregorianCalendar();
			    if (years > 0) {
			      c.add(1, years * (future ? 1 : -1));
			    }
			    if (months > 0) {
			      c.add(2, months * (future ? 1 : -1));
			    }
			    if (weeks > 0) {
			      c.add(3, weeks * (future ? 1 : -1));
			    }
			    if (days > 0) {
			      c.add(5, days * (future ? 1 : -1));
			    }
			    if (hours > 0) {
			      c.add(11, hours * (future ? 1 : -1));
			    }
			    if (minutes > 0) {
			      c.add(12, minutes * (future ? 1 : -1));
			    }
			    if (seconds > 0) {
			      c.add(13, seconds * (future ? 1 : -1));
			    }
			    if (ms > 0) {
				  c.add(14, ms * (future ? 1 : -1));
			    }
			    Calendar max = new GregorianCalendar();
			    max.add(1, 10);
			    if (c.after(max)) {
			      return max.getTimeInMillis();
			    }
			    return c.getTimeInMillis();
			  }
}
