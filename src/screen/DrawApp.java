package screen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import utils.Queue;

public class DrawApp extends Screen {

	WritableImage prevState = null;

	Stack<WritableImage> previousStates;
	Stack<WritableImage> futureStates;

	@Override
	public void start() {
		components = new ArrayList<>();
		previousStates = new Stack<>();
		futureStates = new Stack<>();

		ToggleGroup tools = new ToggleGroup();
		RadioButton paintBrushBtn = new RadioButton("Paint Brush");
		paintBrushBtn.setToggleGroup(tools);
		paintBrushBtn.setSelected(true);

		RadioButton paintBucketBtn = new RadioButton("Paint Bucket");
		paintBucketBtn.setToggleGroup(tools);

		RadioButton lineToolBtn = new RadioButton("Draw Line");
		lineToolBtn.setToggleGroup(tools);

		ColorPicker cPicker = new ColorPicker();

		Spinner<Integer> brushSize = new Spinner<Integer>(0, 64, 4);

		Canvas canvas = new Canvas(1280, 650);
		HBox drawWindow = new HBox(10);
		drawWindow.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		Button saveBtn = new Button("Save");
		saveBtn.setOnAction(e -> {
			saveImage(canvas);
		});

		Button undoBtn = new Button("Undo");
		undoBtn.setOnAction(e -> {
			undo(canvas);
			undo(canvas);
		});

		Button redoBtn = new Button("Redo");
		redoBtn.setOnAction(e -> {
			redo(canvas);
			redo(canvas);
		});

		HBox header = new HBox(8);
		header.setPrefWidth(1280);
		header.getChildren().addAll(paintBrushBtn, paintBucketBtn, lineToolBtn, cPicker, brushSize, saveBtn, undoBtn,
				redoBtn);
		header.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));

		VBox content = new VBox(10);
		content.getChildren().addAll(header, drawWindow);
		content.setAlignment(Pos.TOP_LEFT);

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		Line line = new Line();

		canvas.setOnMousePressed(mouse -> {
			addPreviousState(canvas);
			gc.setLineWidth((double) brushSize.getValueFactory().getValue());
			gc.setStroke(cPicker.getValue());
			if (paintBrushBtn.isSelected()) {
				gc.beginPath();
				gc.lineTo(mouse.getX(), mouse.getY());
				prevState = convertToImage(canvas);
			} else if (paintBucketBtn.isSelected()) {
				gc.drawImage(paintBucket(cPicker.getValue(), (int) mouse.getX(), (int) mouse.getY(), canvas), 0, 0);
				prevState = convertToImage(canvas);
			} else if (lineToolBtn.isSelected()) {
				line.setStartX(mouse.getX());
				line.setStartY(mouse.getY());
			}
		});

		canvas.setOnMouseDragged(mouse -> {
			if (paintBrushBtn.isSelected()) {
				gc.strokeOval(mouse.getX(), mouse.getY(), (double) brushSize.getValueFactory().getValue(),
						(double) brushSize.getValueFactory().getValue());
				gc.lineTo(mouse.getX(), mouse.getY());
			} else if (paintBucketBtn.isSelected()) {
				// TODO: Paint Bucket Implementation
			} else if (lineToolBtn.isSelected()) {
				gc.drawImage(prevState, 0, 0);
				prevState = convertToImage(canvas);
				gc.strokeLine(line.getStartX(), line.getStartY(), mouse.getX(), mouse.getY());
			}
		});

		canvas.setOnMouseReleased(mouse -> {
			addPreviousState(canvas);
			if (paintBrushBtn.isSelected()) {
				gc.strokeOval(mouse.getX(), mouse.getY(), (double) brushSize.getValueFactory().getValue(),
						(double) brushSize.getValueFactory().getValue());
				gc.lineTo(mouse.getX(), mouse.getY());
				gc.closePath();
				prevState = convertToImage(canvas);
			} else if (paintBucketBtn.isSelected()) {
				// TODO: Paint Bucket Implementation
			} else if (lineToolBtn.isSelected()) {
				gc.drawImage(prevState, 0, 0);
				gc.strokeLine(line.getStartX(), line.getStartY(), mouse.getX(), mouse.getY());
				prevState = convertToImage(canvas);
			}
		});

		components.add(content);

	}

	private WritableImage convertToImage(Canvas c) {
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		WritableImage wi = c.snapshot(sp, null);

		return wi;
	}

	private void addPreviousState(Canvas c) {
		if (!previousStates.isEmpty())
			if (convertToImage(c).equals(previousStates.peek()))
				return;

		previousStates.push(convertToImage(c));
	}

	private void addFutureState(Canvas c) {
		futureStates.push(convertToImage(c));
	}

	private void undo(Canvas c) {
		if (!previousStates.isEmpty()) {
			futureStates.push(previousStates.peek());
			c.getGraphicsContext2D().drawImage(previousStates.pop(), 0, 0);
		}
	}

	private void redo(Canvas c) {
		if (!futureStates.isEmpty()) {
			previousStates.push(futureStates.peek());
			c.getGraphicsContext2D().drawImage(futureStates.pop(), 0, 0);
		}
	}

	private WritableImage paintBucket(Color c, int startX, int startY, Canvas canvas) {
		WritableImage wi = convertToImage(canvas);
		Color startC = wi.getPixelReader().getColor(startX, startY);

		wi = floodFill(startX, startY, startC, c, wi);

		return wi;
	}

	private WritableImage floodFill(int x, int y, Color startC, Color newC, WritableImage img) {

		// Check if this pixel is the same colour as the starting pixel

		PixelReader pr = img.getPixelReader();
		PixelWriter pw = img.getPixelWriter();

		if (startC == newC)
			return img;
		if (!pr.getColor(x, y).equals(startC))
			return img;

		Queue<int[]> queue = new Queue<int[]>();
		pw.setColor(x, y, newC);
		int[] node = new int[2];
		node[0] = x;
		node[1] = y;
		queue.enqueue(node);

		while (!queue.isEmpty()) {
			int[] n = queue.peek();
			queue.dequeue();

			// Check West
			if (n[0] - 1 > 0)
				if (pr.getColor(n[0] - 1, n[1]).equals(startC)) {
					pw.setColor(n[0] - 1, n[1], newC);
					int[] newNode = new int[2];
					newNode[0] = n[0] - 1;
					newNode[1] = n[1];
					queue.enqueue(newNode);
				}

			// Check East
			if (n[0] + 1 < img.getWidth())
				if (pr.getColor(n[0] + 1, n[1]).equals(startC)) {
					pw.setColor(n[0] + 1, n[1], newC);
					int[] newNode = new int[2];
					newNode[0] = n[0] + 1;
					newNode[1] = n[1];
					queue.enqueue(newNode);
				}

			// Check North
			if (n[1] - 1 > 0)
				if (pr.getColor(n[0], n[1] - 1).equals(startC)) {
					pw.setColor(n[0], n[1] - 1, newC);
					int[] newNode = new int[2];
					newNode[0] = n[0];
					newNode[1] = n[1] - 1;
					queue.enqueue(newNode);
				}

			// Check South
			if (n[1] + 1 < img.getHeight())
				if (pr.getColor(n[0], n[1] + 1).equals(startC)) {
					pw.setColor(n[0], n[1] + 1, newC);
					int[] newNode = new int[2];
					newNode[0] = n[0];
					newNode[1] = n[1] + 1;
					queue.enqueue(newNode);
				}
		}

		return img;
	}

	private void saveImage(Canvas c) {
		WritableImage img = convertToImage(c);
		File file = new File("./data/images/test.png");

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
