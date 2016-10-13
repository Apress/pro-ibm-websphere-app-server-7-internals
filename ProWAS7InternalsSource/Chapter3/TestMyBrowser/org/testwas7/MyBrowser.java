package org.testwas7;

import org.eclipse.swt.*;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.core.runtime.IPlatformRunnable;

/*
 * Simple Browser
*/

// IPlatformRunnable makes this an Eclipse application
public class MyBrowser implements IPlatformRunnable {

    // The IPlatformRunnable interface that makes this an eclipse application
    // has only a run method that takes an Object as its parameter list
    public Object run(Object args) throws Exception {

	    // Create a sizable application Window for the platform
        // that we are going to fill with a single control
	    Display display = new Display();
        final Shell shell = new Shell(display, SWT.SHELL_TRIM);
        shell.setLayout(new FillLayout());

        // Create a browser control that shows the title
        // of the page we are looking at in the tite bar
	    Browser browser = new Browser(shell, SWT.NONE);
        browser.addTitleListener(new TitleListener() {
			public void changed(TitleEvent event) {
                shell.setText(event.title);
            }
        });

	    // Open the browser window inside the shell window
        browser.setBounds(0, 0, 640, 480);
        shell.pack();
        shell.open();

        browser.setUrl("http://www.eclipse.org");

        // Process events while we have them and while they are not telling
        // use to close the window and exit
        while(!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // We have finished so exit happily
        return EXIT_OK;
    }
}
    

