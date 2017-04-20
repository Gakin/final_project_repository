package cs567.player;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import cs567.decode.BaseDecoder;
import cs567.rep.base.BaseVideo;


public class VideoPlayer {
	JFrame frame;
	JLabel lbImg;
	JLabel lbText;
	
	JButton bPausePlay;
	JButton bRestart;
	JToggleButton bGaze;
	JLabel lbMouse;
	
	GridBagConstraints textConstraint;
	GridBagConstraints imgConstraint;
	GridBagConstraints controlConstraint;
	
	BaseDecoder decoder;
	double frameRate;
	int nextFrame;
	boolean videoPlaying;
	
	Timer timer;
	TimerTask videoPlaybackTask;
	
	String title;
	String detail;
	
	boolean applyGazeControl;
	
	public static enum Property {
		MX,
		MY
	}
	
	public VideoPlayer() {
		this("Video Player");
	}
	
	public VideoPlayer(String t) {
		this.title = t;
	}
	
	private void initUI() {
		frame = new JFrame();
		frame.setTitle(this.title);
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);
		
		textConstraint = new GridBagConstraints();
		textConstraint.fill = GridBagConstraints.HORIZONTAL;
		textConstraint.anchor = GridBagConstraints.CENTER;
		textConstraint.weightx = 0.5;
		textConstraint.gridx = 0;
		textConstraint.gridy = 0;

		imgConstraint = new GridBagConstraints();
		imgConstraint.fill = GridBagConstraints.HORIZONTAL;
		imgConstraint.gridx = 0;
		imgConstraint.gridy = 1;
		
		controlConstraint = new GridBagConstraints();
		controlConstraint.fill = GridBagConstraints.HORIZONTAL;
		controlConstraint.gridx = 0;
		controlConstraint.gridy = 2;	
		
		initControlBar();
	}
	
	private void initControlBar() {
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(1, 3));
		
		bPausePlay = new JButton("||");
		bPausePlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (videoPlaying) {
					pauseVideo();
					bPausePlay.setText(">");
				} else {
					playVideo();
					bPausePlay.setText("||");
				}
			}
		});
		
		controlPanel.add(bPausePlay);
		
		bRestart = new JButton("|<<");
		bRestart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				nextFrame = 0;
			}
		});
		
		controlPanel.add(bRestart);
		
		bGaze = new JToggleButton("Gaze Control");
		bGaze.setSelected(isApplyGazeControl());
		
		bGaze.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isApplyGazeControl()) {
					setApplyGazeControl(false);
					bGaze.setSelected(false);
				} else {
					setApplyGazeControl(true);
					bGaze.setSelected(true);
				}
			}
		});
		
		controlPanel.add(bGaze);
		
		
		lbMouse = new JLabel("Mouse Position: N/A");
		controlPanel.add(lbMouse);
		
		frame.getContentPane().add(controlPanel, controlConstraint);
	}
	
	public void setDecoder(BaseVideo v, BaseDecoder d, double fr) {
		initUI();
		this.decoder = d;
		this.frameRate = fr;
		
		this.decoder.init(v);
		
		detail = String.format("Video height: %d, width: %d, count: %d, fr: %.1f", decoder.getHeight(), decoder.getWidth(), decoder.getFrameCount(), frameRate);
		lbText = new JLabel(detail);
		lbText.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lbText, textConstraint);
		
		lbImg = new JLabel();
		frame.getContentPane().add(lbImg, imgConstraint);
		updateImg(new BufferedImage(decoder.getWidth(), decoder.getHeight(), BufferedImage.TYPE_INT_RGB));
//		System.out.println(String.format("Decoder width:%d  height:%d", decoder.getWidth(), decoder.getHeight()));
		frame.pack();
		
		lbImg.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				displayMousePosition(getMousePosition());
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void updateImg(BufferedImage bm) {
		Icon ic = new ImageIcon(bm);
		lbImg.setIcon(ic);
		frame.revalidate();
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	public void playVideo() {
		int period = (int)(1000/frameRate);
		timer = new Timer();
		
		videoPlaybackTask = new TimerTask() {
			boolean start = true;
			@Override
			public void run() {
				Point mpos = getMousePosition();
				
				if (!isApplyGazeControl() || mpos == null) {
					decoder.prepareFrame(nextFrame, null);
				} else {
					Map params = new HashMap<>();
					params.put(VideoPlayer.Property.MX, (int)mpos.getX());
					params.put(VideoPlayer.Property.MY, (int)mpos.getY());
					decoder.prepareFrame(nextFrame, params);
				}
				
				BufferedImage bm = decoder.getFrame(nextFrame);
				lbText.setText(detail + " @" + nextFrame); 
				updateImg(bm);
				
				if (start) {
					show();
					start = false;
				}
				nextFrame = (nextFrame+1) % decoder.getFrameCount();
			}
		};
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				pauseVideo();
				System.out.println("Exitting Program");
				System.exit(0);
			}
		});
		
		videoPlaying = true;	
		timer.scheduleAtFixedRate(videoPlaybackTask, 0, period);
	}
	
	public void pauseVideo() {
		videoPlaybackTask.cancel();
		timer.cancel();
		timer.purge();
		videoPlaying = false;
	}
	
	public Point getMousePosition() {
		return lbImg.getMousePosition();
	}
	
	private void displayMousePosition(Point p) {
		if(p == null) {
			lbMouse.setText("Mouse Position: N/A");
		} else {
			lbMouse.setText(String.format("Mouse Position: %d,%d", (int) p.getX(), (int) p.getY()));
		}
	}

	public boolean isApplyGazeControl() {
		return applyGazeControl;
	}

	public void setApplyGazeControl(boolean applyGazeControl) {
		this.applyGazeControl = applyGazeControl;
	}
	
	
}
