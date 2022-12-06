package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void Compression() throws FileNotFoundException {
        Scanner cin = new Scanner(new FileInputStream("x.txt"));
        PrintWriter printWriter = new PrintWriter("y.txt");

        //printWriter.println("Enter your Data: ");
        String s=cin.nextLine();
        Map<Character,Double> probability = new HashMap<Character,Double>();
        for (int i=0;i<s.length();i++)
        {
            probability.put(s.charAt(i),0.0);
        }
        double Smallest_Range=1000;
        //printWriter.println("Enter probabilities for each char ex: (a 0.2) ");
        for(Map.Entry<Character,Double> current:probability.entrySet())
        {
            //printWriter.print(current.getKey()+" ");
            double prob= cin.nextDouble();
            current.setValue(prob);
            Smallest_Range=Math.min(Smallest_Range,prob);
        }
        //Find the Min number of Bits required to store a Code LESS than the Smallest Range
        int k=0;
        for(int i=1;i<=100;i++)
        {
            if(1/Math.pow(2,i)<=Smallest_Range)
            {
                k=i;
                break;
            }
        }
        //build Low Range and High Range for each Character
        Map<Character, Pair>low_high = new HashMap<Character, Pair>();
        double last=0;
        for(Map.Entry<Character,Double> current:probability.entrySet())
        {
            low_high.put(current.getKey(),new Pair(last,last+current.getValue()));
            last=last+current.getValue();
        }

        String stream="";
        //first char
        double lastLow= low_high.get(s.charAt(0)).low;
        double lastHigh= low_high.get(s.charAt(0)).high;
        double lower=lastLow;
        for(int i=1;i<s.length();i++)
        {
            lower=lastLow;
            lastLow=lower+(lastHigh-lower)*low_high.get(s.charAt(i)).low;
            lastHigh=lower+(lastHigh-lower)*low_high.get(s.charAt(i)).high;
            while ((0.5<lastLow &&.5<lastHigh)|| (0.5>lastLow &&.5>lastHigh))
            {
                if(0.5<lastLow &&.5<lastHigh)
                {
                    stream+='1';
                    lastLow=(lastLow-.5)*2;
                    lastHigh=(lastHigh-.5)*2;
                }
                else
                {
                    stream+='0';
                    lastLow*=2;
                    lastHigh*=2;

                }
            }
        }
        stream+='1';
        double compressedCode=0;
        int ind=stream.length()-1;
        //takeeeeee careeeeeeeeeeee!!!
        for (int i=0;i<stream.length();i++)
        {
            if(stream.charAt(i)=='1')
                compressedCode+=(Math.pow(2,ind));
            ind--;
        }
        compressedCode=(compressedCode)/(Math.pow(2,stream.length()));
        for(int i=0;i<k-1;i++)
        {
            stream+='0';
        }
        printWriter.println("Stream: "+stream);
        printWriter.println("Compressed Code is equivalent to: "+ String.format("%.20f", compressedCode));
        printWriter.close();
    }


    public static void Decompression() throws FileNotFoundException {
        Scanner cin = new Scanner(new FileInputStream("x.txt"));
        PrintWriter printWriter = new PrintWriter("y.txt");
        //printWriter.println("Enter Stream of data : ");
        String stream=cin.next();
        double Smallest_Range=1000;
        Map<Character,Double> probability = new HashMap<Character,Double>();
        //printWriter.println("Enter number of distinct Character ");
        int n=cin.nextInt();
        //printWriter.println("Enter probabilities for each char ex: (a 0.2) ");
        for(int i=0;i<n;i++)
        {
            char c=cin.next().charAt(0);
            double prob= cin.nextDouble();
            probability.put(c,prob);
            Smallest_Range=Math.min(Smallest_Range,prob);
        }
        //Find the Min number of Bits required to store a Code LESS than the Smallest Range
        int k=0;
        for(int i=1;i<=100;i++)
        {
            if(1/Math.pow(2,i)<=Smallest_Range)
            {
                k=i;
                break;
            }
        }
        //build Low Range and High Range for each Character
        Map<Character, Pair>low_high = new HashMap<Character, Pair>();
        double last=0;
        for(Map.Entry<Character,Double> current:probability.entrySet())
        {
            low_high.put(current.getKey(),new Pair(last,last+current.getValue()));
            last=last+current.getValue();
        }
        String firstK=stream.substring(0,k);
        int r=k;
        String orginal="";
        double lastLow=0,lastHigh=1;
        while (r<=stream.length())
        {
            //System.out.println(firstK);
            double curr=0;
            int ind=firstK.length()-1;
            //takeeeeee careeeeeeeeeeee!!!
            for (int i=0;i<firstK.length();i++)
            {
                if(firstK.charAt(i)=='1')
                    curr+=(Math.pow(2,ind));
                ind--;
            }
            curr=(curr)/(Math.pow(2,firstK.length()));
            curr=(curr-lastLow)/(lastHigh-lastLow);
            for(Map.Entry<Character, Pair> current:low_high.entrySet())
            {
                if(curr>=current.getValue().low&&curr<=current.getValue().high)
                {
                    orginal+=current.getKey();
                    break;
                }
            }
            if(r==stream.length())
                break;
            double lower=lastLow;
            lastLow=lower+(lastHigh-lower)*low_high.get(orginal.charAt(orginal.length()-1)).low;
            lastHigh=lower+(lastHigh-lower)*low_high.get(orginal.charAt(orginal.length()-1)).high;
            while ((0.5<lastLow &&.5<lastHigh)|| (0.5>lastLow &&.5>lastHigh))
            {
                firstK=firstK.substring(1);
                firstK+=stream.charAt(r);
                r++;
                if(0.5<lastLow &&.5<lastHigh)
                {
                    lastLow=(lastLow-.5)*2;
                    lastHigh=(lastHigh-.5)*2;
                }
                else
                {
                    lastLow*=2;
                    lastHigh*=2;

                }
            }


        }
        printWriter.println(orginal);
        printWriter.close();
    }

    public static void main(String[] args) {
        JFrame frame_ = new JFrame("Binary Arithmetic");
        JLabel label_1, label_2;
        label_1 = new JLabel("Enter Input File Name");
        label_1.setBounds(50, 20, 300, 25);
        final JTextField input1 = new JTextField();
        input1.setBounds(190, 25, 150, 25);

        label_2 = new JLabel("Enter Output File Name");
        label_2.setBounds(50, 70, 300, 25);
        final JTextField input2 = new JTextField();
        input2.setBounds(190, 70, 150, 25);

        JButton button1 = new JButton("Compression");
        button1.setBounds(350, 150, 150, 30);
        JButton button2 = new JButton("Decompression");
        button2.setBounds(150, 150, 150, 30);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Compression();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);


            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Decompression();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                //decompression();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);


            }
        });
        frame_.add(label_1);
        frame_.add(label_2);
        frame_.add(button1);
        frame_.add(button2);
        frame_.add(input1);
        frame_.add(input2);
        frame_.setSize(600, 250);
        frame_.setLayout(null);
        frame_.setVisible(true);




    }
}
/*
acba
0.8
0.02
0.18


110001100000
3
a 0.8
b 0.02
c 0.18

---------------------------
000001
1
a 0.8



aaaaaaaaaaaaaaaaa
0.8


*/