package net.fabricmc.fabric.security.checks;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.JoinWorldEvent;
import net.fabricmc.fabric.security.Networking;

public class SecurityManager {
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final PrintStream nullStream = new PrintStream(OutputStream.nullOutputStream());
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Thread checkThread;
    public static boolean e = false;

    public void blockPrint() {
    }

    public void unblockPrint() {
        System.setOut(this.originalOut);
        System.setErr(this.originalErr);
    }

    public void startChecks() {
    }

    public void stopChecks() {
    }

    @EventHandler
    public void onJoin(JoinWorldEvent event) {
    }
}
