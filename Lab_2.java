package com.company;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        List<BigInteger> keyA = GenerateKeyPair();
        BigInteger exp = keyA.get(3);
        BigInteger n = keyA.get(4);
        BigInteger d = keyA.get(2);

        System.out.println("1 - to encrypt");
        System.out.println("2 - to decrypt");
        System.out.println("3 - to sign");
        System.out.println("4 - to verify");
        System.out.println("5 - to send key ");
        System.out.println("6 - to receive key");
        System.out.println("7 - to exit");

        BigInteger M = BigInteger.ZERO;
        BigInteger S = BigInteger.ZERO;
        BigInteger C = BigInteger.ZERO;
        List<BigInteger> sKeyList = new ArrayList<BigInteger>();
        Scanner in = new Scanner(System.in);
        int choose = 0;
        while (choose != 7)
        {
            choose = in.nextInt();
            switch (choose)
            {
                case 1:
                    M = enterMsg();
                    C = Encrypt(exp, n, M);
                    break;
                case 2:
                    M = Decrypt(n, d, C);
                    break;
                case 3:
                    M = enterMsg();
                    S = Sign(M, d, n);
                    break;
                case 4:
                    Verify(S, exp, n, M);
                    break;
                case 5:
                    sKeyList = SendKey(n, d);
                    break;
                case 6:
                    RecieveKey(sKeyList.get(2), sKeyList.get(3), sKeyList.get(0), sKeyList.get(1), exp, n);
                    break;
                default:
                    if(choose != 7)
                        System.out.println("try again");
                    break;
            }
        }
    }
    static BigInteger Encrypt(BigInteger e, BigInteger n, BigInteger msg)
    {
        BigInteger C = msg.modPow(e,n);

        String sN = n.toString(16);
        String sE = e.toString(16);
        String sM = msg.toString(16);
        String sC = C.toString(16);
        System.out.println("n = " + sN);
        System.out.println("e = " + sE);
        System.out.println("M = " + sM);
        System.out.println("C = " + sC);

        return C;
    }

    static BigInteger Decrypt(BigInteger n, BigInteger d, BigInteger C)
    {
        BigInteger M = C.modPow(d, n);

        String sN = n.toString(16);
        String sD = d.toString(16);
        String sC = C.toString(16);
        String sM = M.toString(16);
        System.out.println("n = " + sN);
        System.out.println("d = " + sD);
        System.out.println("C = " + sC);
        System.out.println("M = " + sM);

        return M;
    }

    static BigInteger Sign(BigInteger msg, BigInteger d , BigInteger n)
    {
        BigInteger S = msg.modPow(d, n);

        String sS = S.toString(16);
        String sN = n.toString(16);
        String sD = d.toString(16);
        String sM = msg.toString(16);
        System.out.println("M = " + sM);
        System.out.println("n = " + sN);
        System.out.println("d = " + sD);
        System.out.println("S = " + sS);

        return S;
    }

    static void Verify(BigInteger S , BigInteger exp , BigInteger n , BigInteger M)
    {
        BigInteger Mcheck = S.modPow(exp, n);

        String sN = n.toString(16);
        String sE = exp.toString(16);
        String sS = S.toString(16);
        System.out.println("n = " + sN);
        System.out.println("e = " + sE);
        System.out.println("S = " + sS);

        if (Mcheck.equals(M))
            System.out.println("Signature OK");
        else
            System.out.println("Signature NOT OK!");
    }

    static List SendKey(BigInteger n, BigInteger d)
    {
        List<BigInteger> keyB = GenerateKeyPair();
        BigInteger n1 = keyB.get(4);

        while(n.compareTo(n1) != -1)
            keyB = GenerateKeyPair();

        n1 = keyB.get(4);
        BigInteger e1 = keyB.get(3);
        BigInteger k = nextRandomBigInteger(n);
        BigInteger S = k.modPow(d, n);
        BigInteger k1 = k.modPow(e1, n1);
        BigInteger s1 = S.modPow(e1, n1);

        String sN = n.toString(16);
        String sD = d.toString(16);
        String sN1 = n1.toString(16);
        String sE1 = e1.toString(16);
        String sk = k1.toString(16);
        String sS = s1.toString(16);
        String SK = k.toString(16);
        String SS = S.toString(16);

        System.out.println("Random k = " + SK);
        System.out.println("n = " + sN);
        System.out.println("d = " + sD);
        System.out.println("n1 = " + sN1);
        System.out.println("e1 = " + sE1);
        System.out.println("k1 = " + sk);
        System.out.println("s1 = " + sS);
        System.out.println("MSG = " + SS);

        BigInteger d1 = keyB.get(2);
        List<BigInteger> k1s1 = new ArrayList<BigInteger>();
        k1s1.add(k1);
        k1s1.add(s1);
        k1s1.add(n1);
        k1s1.add(d1);
        return k1s1;
    }

    static void RecieveKey(BigInteger n1, BigInteger d1, BigInteger k1, BigInteger s1, BigInteger e, BigInteger n)
    {
        BigInteger kb = k1.modPow(d1, n1);
        BigInteger sb = s1.modPow(d1, n1);
        BigInteger k = sb.modPow(e, n);

        String sN1 = n1.toString(16);
        String sD1 = d1.toString(16);
        String sN = n.toString(16);
        String sE = e.toString(16);
        String sk1 = k1.toString(16);
        String sS1 = s1.toString(16);
        String skb = kb.toString(16);
        String sSb = sb.toString(16);
        String kk = k.toString(16);

        System.out.println("n1 = " + sN1);
        System.out.println("d1 = " + sD1);
        System.out.println("n = " + sN);
        System.out.println("e = " + sE);
        System.out.println("k1 = " + sk1);
        System.out.println("s1 = " + sS1);
        System.out.println("k conf = " + skb);
        System.out.println("S = " + sSb);
        System.out.println("k aut = " + kk);
        //if (kb.equals(k))
        //    System.out.println("Confidential");
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
        //keyPairs.add(euler);
        return keyPairs;
    }
    static BigInteger BigPrime()
    {
        int[] l89 = L89();
        BigInteger a = BigInteger.ZERO;
        String str = "";
        for (int i = 0; i < l89.length; i++)
        {
            if(i % 128 == 0 && i != 0)
            {
                a = new BigInteger(str, 2);
                Boolean res = MillerRabinTest(a , 1024);
                if(res == true && a.bitLength() == 128)
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

    static BigInteger enterMsg()
    {
        System.out.println("Enter your message");
        int message = 0;
        Scanner in = new Scanner(System.in);
        message = in.nextInt();
        BigInteger msg = BigInteger.valueOf(message);

        return msg;
    }
}