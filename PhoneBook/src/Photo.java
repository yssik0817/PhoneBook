import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Photo extends JFrame {
	static int[][] inImage = new int[512][512];
	static int[][] outImage = new int[512][512];

	Container contentPane;

	public static void main(String[] args) throws Exception {

		loadImage();

		new Photo();
	}

	static public void loadImage() throws Exception {
		int i, k;

		File inFp; // 파일 핸들
		FileInputStream inFs; // 파일 스트림 핸들
		inFp = new File("prince.raw");

		// 읽어올 파일 스크림
		inFs = new FileInputStream(inFp.getPath());

		// 파일 --> 메모리
		for (i = 0; i < 512; i++) {
			for (k = 0; k < 512; k++) {
				inImage[i][k] = inFs.read();
				outImage[i][k] = inImage[i][k];
			}
		}
		inFs.close();
	}

	// 생성자 - 메뉴추가, 판넬 부착
	Photo() {
		setTitle("사진 처리 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPane = getContentPane();

		// 메뉴 추가
		addMenu();

		// 패널 추가
		DrawImage panel = new DrawImage();
		contentPane.add(panel, BorderLayout.CENTER);

		// 입,출력 중 높은 이미지를 최대 높이로 처리
		setSize(8 + 512 + 8, 25 + 31 + 512 + 8);
		setVisible(true);

		displayImage();
	}

	// 패널 --> 입,출력 이미지 출력
	class DrawImage extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int R, G, B;
			int i, k;
			for (i = 0; i < 512; i++) {
				for (k = 0; k < 512; k++) {
					R = G = B = (int) outImage[i][k];
					g.setColor(new Color(R, G, B));
					g.drawRect(k, i, 1, 1);
				}
			}

		}
	}

	void displayImage() {
		Graphics g = contentPane.getGraphics();
		contentPane.paintAll(g);
	}

	public void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu photoMenu = new JMenu("사진처리");
		menuBar.add(photoMenu);

		JMenuItem equalAction = new JMenuItem("동일한 사진");
		JMenuItem negativeAction = new JMenuItem("반전된 사진");
		JMenuItem mirror1Action = new JMenuItem("좌우대칭 사진");
		JMenuItem mirror2Action = new JMenuItem("상하대칭 사진");
		JMenuItem saveAction = new JMenuItem("저장");
		JMenuItem exitAction = new JMenuItem("Exit");

		photoMenu.add(equalAction);
		photoMenu.add(negativeAction);
		photoMenu.add(mirror1Action);
		photoMenu.add(mirror2Action);
		photoMenu.addSeparator();
		photoMenu.add(saveAction);
		photoMenu.add(exitAction);

		// 동일이미지 처리
		equalAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				equal();
			}
		});

		// 반전 영상 처리
		negativeAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				negative();
			}
		});

		// 좌우 대칭 처리
		mirror1Action.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mirror1();
			}
		});

		// 상하 대칭 처리
		mirror2Action.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mirror2();
			}
		});

		// 파일 저장
		saveAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveImage();
			}
		});

		exitAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}

	void equal() {
		int i, k;
		for (i = 0; i < 512; i++)
			for (k = 0; k < 512; k++)
				outImage[i][k] = inImage[i][k];

		displayImage();
	}

	void negative() {
		int i, k;
		for (i = 0; i < 512; i++)
			for (k = 0; k < 512; k++)
				outImage[i][k] = 255 - inImage[i][k];

		displayImage();
	}

	void mirror1() {
		int i, k;
		for (i = 0; i < 512; i++)
			for (k = 0; k < 512; k++)
				outImage[i][k] = inImage[i][511 - k];

		displayImage();
	}

	void mirror2() {
		int i, k;
		for (i = 0; i < 512; i++)
			for (k = 0; k < 512; k++)
				outImage[i][k] = inImage[511 - i][k];

		displayImage();
	}

	public void saveImage() {
		int i, k;

		String newFname = "c:\\temp\\result.raw";
		File outFp; // 파일 핸들
		FileOutputStream outFs; // 파일 스트림 핸들

		outFp = new File(newFname);

		// 저장할 파일 스크림
		try {
			outFs = new FileOutputStream(outFp.getPath());

			// 메모리 --> 파일
			for (i = 0; i < 512; i++) {
				for (k = 0; k < 512; k++) {
					outFs.write(outImage[i][k]);
				}
			}
			outFs.close();
			JOptionPane.showMessageDialog(null, "파일 저장 성공", "파일 저장", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
