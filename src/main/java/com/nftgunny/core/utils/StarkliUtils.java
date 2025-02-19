package com.nftgunny.core.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class StarkliUtils {
    public void testCommandLine() {
        try {
            // Lệnh cần chạy
            String command = "starkli call" +
                    "     0x02ceb6da76e2b2b80f2a23ff2910edf1dcc12e1b9d6a27d1da43d7db943e568c" +
                    "     get_balance" +
                    "     --rpc=http://127.0.0.1:5050";

            // Tạo process chạy lệnh
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();

            // Đọc output từ process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Đợi process kết thúc
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
