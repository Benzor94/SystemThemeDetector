package hu.benzor.systemthemedetector.internal.mode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.benzor.systemthemedetector.theme.Theme.Mode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public final class LinuxModeDetector extends ModeDetector {

    private final Pattern cmdOutputPattern = Pattern.compile("\\(<uint32 (\\d+)>,\\)");
    private final Pattern monitorOutputPattern = Pattern.compile("variant\\s+uint32\\s+(\\d+)");

    @Override
    protected ProcessBuilder getCommandProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "gdbus",
            "call",
            "--session",
            "--timeout=1000",
            "--dest=org.freedesktop.portal.Desktop",
            "--object-path=/org/freedesktop/portal/desktop",
            "--method=org.freedesktop.portal.Settings.ReadOne",
            "org.freedesktop.appearance",
            "color-scheme"
        );
        return pb;
    }

    @Override
    protected ProcessBuilder getMonitorProcessBuilder() {
        ProcessBuilder pb = new ProcessBuilder(
            "dbus-monitor",
            "type='signal',interface='org.freedesktop.portal.Settings',arg0='org.freedesktop.appearance',arg1='color-scheme'"
        );
        return pb;
    }

    @Override
    protected Mode getModeFromCommandOutput(String output) {
        /*
         * Here we expect strings of the form "(<<uint32 n>>,)" where n is an unsigned integer,
         * and we want to extract this integer.
         */
        if (output == null) {
            log.warn("Null mode string received.");
            return Mode.APP_DEFAULT;
        }
        Matcher matcher = cmdOutputPattern.matcher(output);
        return extractModeFromMatcher(output, matcher);
        
    }

    @Override
    protected Mode getModeFromMonitorOutput(String output) {
        /*
         * Here we expect strings of the form "    variant     uint32 n", where we do not
         * constrain the number of whitespace characters, and n is an unsigned
         * integer that we want to extract.
         */
        if (output == null) {
            log.warn("Null mode string received");
            return Mode.APP_DEFAULT;
        }
        output = output.trim();
        Matcher matcher = monitorOutputPattern.matcher(output);
        return extractModeFromMatcher(output, matcher);
    }

    private Mode extractModeFromMatcher(String output, Matcher matcher) {
        if (!matcher.matches()) {
            log.warn("Invalid mode string received: {}.", output);
            return Mode.APP_DEFAULT;
        }
        String modeString = matcher.group(1);
        try {
            int modeNumber = Integer.parseInt(modeString);
            if (modeNumber < 0 || modeNumber > 2) {
                log.warn("Unrecognited mode number {}.", modeNumber);
            }
            return Mode.fromId(modeNumber);
        } catch (NumberFormatException e) {
            log.warn("Invalid mode string received: {}.", output);
            return Mode.APP_DEFAULT;
        }
    }

}
