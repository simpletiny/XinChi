package apptest;

import java.util.Arrays;

public class RC4 {
	private int[] S = new int[256];
	private int[] T = new int[256];

	public RC4(byte[] key) {
		for (int i = 0; i < 256; i++) {
			S[i] = i;
			T[i] = key[i % key.length];
		}

		int j = 0;
		for (int i = 0; i < 256; i++) {
			j = (j + S[i] + T[i]) % 256;
			swap(S, i, j);
		}
	}

	public byte[] encrypt(byte[] plaintext) {
		byte[] ciphertext = new byte[plaintext.length];
		int i = 0, j = 0;

		for (int k = 0; k < plaintext.length; k++) {
			i = (i + 1) % 256;
			j = (j + S[i]) % 256;
			swap(S, i, j);
			int t = (S[i] + S[j]) % 256;
			ciphertext[k] = (byte) (plaintext[k] ^ S[t]);
		}

		return ciphertext;
	}

	public byte[] decrypt(byte[] ciphertext) {
		return encrypt(ciphertext);
	}

	private void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public static void main(String[] args) {
		byte[] key = "Key".getBytes();
		byte[] plaintext = "Plaintext".getBytes();

		RC4 rc4 = new RC4(key);
		byte[] ciphertext = rc4.encrypt(plaintext);
		byte[] decrypted = rc4.decrypt(ciphertext);

		System.out.println("Key: " + Arrays.toString(key));
		System.out.println("Plaintext: " + Arrays.toString(plaintext));
		System.out.println("Ciphertext: " + Arrays.toString(ciphertext));
		System.out.println("Decrypted: " + Arrays.toString(decrypted));
	}
}
