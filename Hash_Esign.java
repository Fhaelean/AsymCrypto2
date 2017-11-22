package com.company;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.security.*;
import java.security.MessageDigest;

public class Main
{
    public static void main(String[] args)throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        List<BigInteger> keyA = GenerateKeyPair();
        BigInteger p = keyA.get(0);
        BigInteger q = keyA.get(1);

        List<BigInteger> keyB = GenerateKeyPair();
        BigInteger p1 = keyB.get(0);
        BigInteger q1 = keyB.get(1);

        BigInteger pq = p.multiply(q);
        BigInteger p1q1 = p1.multiply(q1);
        BigInteger exp = keyA.get(3);
        BigInteger n = keyA.get(4);
        BigInteger d = keyA.get(2);

        System.out.println("Hello^^");
        System.out.println("Enter your choose");
        System.out.println("1 - for encrypt");
        System.out.println("2 - for generate signature");
        System.out.println("3 - for check signature");
        System.out.println("4 - for dicrypt ");
        System.out.println("5 - for exit ");
        BigInteger M = BigInteger.valueOf(0);
        BigInteger S = BigInteger.valueOf(0);
        BigInteger massege = BigInteger.valueOf(0);
        Scanner in = new Scanner(System.in);
        int choose = 0;
        while (choose != 5) {
            choose = in.nextInt();
            switch (choose) {
                case 1:
                    M = Rsaencrypt(exp, n);
                    break;
                case 2:
                    S = Signature(M, d, n);
                    break;
                case 3:
                    CheakSignature(S, exp, n, M);
                    break;
                case 4:
                    massege = Decript(M, d, n);
                    System.out.println(massege);
                    break;
            }
        }
    }
    static BigInteger Rsaencrypt(BigInteger exp , BigInteger n) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        BigInteger result = BigInteger.valueOf(0) ;

    System.out.println("Enter message");
    int message;
    Scanner in = new Scanner(System.in);
    message = in.nextInt();
    BigInteger msg = BigInteger.valueOf(message);


    BigInteger C = msg.modPow(exp, n);
    result = C;
    return C;
    }
    static BigInteger Signature(BigInteger M , BigInteger d , BigInteger n) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        String m1 = "" + M;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(m1.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(10);
        System.out.println(hashtext);
        BigInteger a = new BigInteger(hashtext);
        BigInteger S = a.modPow(d, n);
        return S;
    }
    static void CheakSignature(BigInteger S , BigInteger exp , BigInteger n , BigInteger M)throws NoSuchAlgorithmException, UnsupportedEncodingException{
        BigInteger K = S.modPow(exp, n);
        String m1 = "" + M;
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(m1.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(10);
        BigInteger a = new BigInteger(hashtext);
        if (a.equals(K))
            System.out.println("Signature OK");
        else
            System.out.println("Signature false");
    }
    static BigInteger Decript(BigInteger M , BigInteger d , BigInteger n) //throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
       BigInteger massege = M.modPow(d, n);
       return massege;
    }
    static List GenerateKeyPair()
    {
        BigInteger p = BigPrime();
        BigInteger q = BigPrime();

        BigInteger euler = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger exp = (BigInteger.valueOf(2).pow(16)).add(BigInteger.ONE);   //e = 2^16 + 1
        BigInteger d = exp.modInverse(euler);
        if((exp.multiply(d)).mod(euler).compareTo(BigInteger.ONE) != 0)
            throw new ArithmeticException("ed != 1 mod n");
        List<BigInteger> keyPairs = new ArrayList<BigInteger>();
        keyPairs.add(p);
        keyPairs.add(q);
        keyPairs.add(d);
        keyPairs.add(exp); //e
        keyPairs.add(p.multiply(q)); //n
        return keyPairs;
    }
    static BigInteger BigPrime()
    {
        int[] l89 = L89();
        BigInteger a = BigInteger.ZERO;
        String str = "";
        for (int i = 0; i < l89.length; i++)
        {
            if(i % 256 == 0 && i != 0)
            {
                a = new BigInteger(str, 2);
                Boolean res = MillerRabinTest(a , 1024);
                if(res == true && a.bitLength() == 256)
                    break;
                str = "";
            }
            str += l89[i];
        }
        return a;
    }

    static int[] L89()
    {
        int[] L89 = new int[8000000];
        for (int i = 0; i < 89; i++)
            L89[i] = (int)(Math.random() * 2);
        for (int i = 0; i < (L89.length - 89); i++)
            L89[89 + i] = (L89[i] ^ L89[i + 51]);
        return L89;
    }

    static Boolean MillerRabinTest(BigInteger p, int k)
    {
        if (p.compareTo(BigInteger.valueOf(2)) == -1)
            return false;
        if (p.compareTo(BigInteger.valueOf(2)) == 0)
            return true;
        if (p.mod(BigInteger.valueOf(2)) == BigInteger.ZERO)
            return false;

        BigInteger r = BigInteger.ZERO;
        BigInteger d = p.subtract(BigInteger.ONE);

        while (d.mod(BigInteger.valueOf(2)) == BigInteger.ZERO)
        {
            d = d.divide(BigInteger.valueOf(2));
            r = r.add(BigInteger.ONE);
        }

        for (int i = 0; i < k; i++)
        {
            BigInteger a = nextRandomBigInteger(p.subtract(BigInteger.valueOf(2)));
            BigInteger x = a.modPow(d, p);
            if (x.compareTo(BigInteger.ONE) == 0 || x.compareTo(p.subtract(BigInteger.ONE)) == 0)
                continue;
            for (int j = 0; BigInteger.valueOf(j).compareTo(r.subtract(BigInteger.ONE)) < 0; j++)
            {
                x = x.modPow(BigInteger.valueOf(2), p);
                if (x.compareTo(BigInteger.ONE) == 0)
                    return false;
                if (x.compareTo(p.subtract(BigInteger.ONE)) == 0)
                    break;
            }
            if (x.compareTo(p.subtract(BigInteger.ONE)) != 0)
                return false;
        }
        return true;
    }

    static BigInteger nextRandomBigInteger(BigInteger n)
    {
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while( result.compareTo(n) >= 0 )
            result = new BigInteger(n.bitLength(), rand);
        if(result != BigInteger.ZERO || result != BigInteger.ONE || result != BigInteger.valueOf(2))
            return result;
        return BigInteger.ZERO;
    }
}
