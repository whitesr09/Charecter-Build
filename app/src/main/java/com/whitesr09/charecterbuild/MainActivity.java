package com.whitesr09.charecterbuild;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends Activity {

    private static final int BG = Color.rgb(10, 11, 16);
    private static final int CARD = Color.rgb(20, 22, 31);
    private static final int CARD_2 = Color.rgb(26, 28, 40);
    private static final int TEXT = Color.rgb(246, 246, 250);
    private static final int MUTED = Color.rgb(167, 170, 184);
    private static final int ACCENT = Color.rgb(157, 123, 255);
    private static final int ACCENT_2 = Color.rgb(105, 213, 255);
    private static final int BORDER = Color.rgb(52, 55, 72);

    private final Random random = new Random();
    private final List<Spinner> randomizable = new ArrayList<>();

    private EditText nameInput;
    private EditText extraInput;
    private Spinner ageSpinner;
    private Spinner genderSpinner;
    private Spinner ethnicitySpinner;
    private Spinner roleSpinner;
    private Spinner buildSpinner;
    private Spinner hairSpinner;
    private Spinner hairColorSpinner;
    private Spinner eyeSpinner;
    private Spinner expressionSpinner;
    private Spinner demeanorSpinner;
    private Spinner outfitSpinner;
    private Spinner environmentSpinner;
    private Spinner lightingSpinner;
    private Spinner lensSpinner;
    private Spinner styleSpinner;
    private Spinner ratioSpinner;
    private Switch fullBodySwitch;
    private TextView outputView;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setStatusBarColor(BG);
        window.setNavigationBarColor(BG);

        prefs = getSharedPreferences("character_build", MODE_PRIVATE);
        setContentView(buildUi());

        String lastPrompt = prefs.getString("last_prompt", "");
        if (!lastPrompt.isEmpty()) {
            outputView.setText(lastPrompt);
        }
    }

    private View buildUi() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(BG);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        LinearLayout root = column();
        root.setPadding(dp(18), dp(18), dp(18), dp(36));
        scrollView.addView(root, matchWrap());

        TextView eyebrow = text("PROMPT STUDIO", 12, ACCENT_2, Typeface.BOLD);
        eyebrow.setLetterSpacing(0.16f);
        root.addView(eyebrow);

        TextView title = text("Character Build", 31, TEXT, Typeface.BOLD);
        title.setPadding(0, dp(5), 0, 0);
        root.addView(title);

        TextView subtitle = text("Build detailed, production-ready AI character prompts in seconds.", 15, MUTED, Typeface.NORMAL);
        subtitle.setPadding(0, dp(6), 0, dp(18));
        root.addView(subtitle);

        LinearLayout identityCard = card("01  CHARACTER");
        root.addView(identityCard, cardParams());

        nameInput = edit("Character name (optional)");
        identityCard.addView(nameInput, fieldParams());

        genderSpinner = addSpinner(identityCard, "Gender", new String[]{
                "Male", "Female", "Androgynous", "Non-binary"
        });
        ageSpinner = addSpinner(identityCard, "Age", new String[]{
                "Child (6–12)", "Teen (13–17)", "Young adult (18–24)", "Adult (25–34)",
                "Adult (35–44)", "Mature adult (45–60)", "Senior (60+)"
        });
        ethnicitySpinner = addSpinner(identityCard, "Appearance / ethnicity", new String[]{
                "Indian", "South Asian", "East Asian", "Southeast Asian", "Middle Eastern",
                "African", "Black", "White / European", "Latino / Hispanic", "Mixed ethnicity"
        });
        roleSpinner = addSpinner(identityCard, "Character role", new String[]{
                "Influencer", "Model", "Student", "Professional", "Entrepreneur", "Athlete",
                "Artist", "Musician", "Traveler", "Fantasy hero", "Sci-fi character"
        });
        buildSpinner = addSpinner(identityCard, "Build", new String[]{
                "Lean", "Athletic", "Slim", "Average", "Curvy", "Muscular", "Broad-shouldered", "Petite"
        });

        LinearLayout appearanceCard = card("02  APPEARANCE");
        root.addView(appearanceCard, cardParams());

        hairSpinner = addSpinner(appearanceCard, "Hair style", new String[]{
                "Short textured", "Medium textured", "Long waves", "Soft curls", "Slick back",
                "Messy fringe", "Buzz cut", "Bob cut", "Braided", "Ponytail"
        });
        hairColorSpinner = addSpinner(appearanceCard, "Hair color", new String[]{
                "Black", "Dark brown", "Chestnut brown", "Light brown", "Blonde", "Auburn", "Silver", "Fantasy color"
        });
        eyeSpinner = addSpinner(appearanceCard, "Eyes", new String[]{
                "Deep brown", "Light brown", "Hazel", "Green", "Blue", "Grey", "Emerald green"
        });
        expressionSpinner = addSpinner(appearanceCard, "Expression", new String[]{
                "Confident smile", "Soft smile", "Neutral", "Serious", "Joyful", "Mysterious",
                "Thoughtful", "Intense gaze", "Calm"
        });
        demeanorSpinner = addSpinner(appearanceCard, "Demeanor", new String[]{
                "Confident", "Elegant", "Charismatic", "Friendly", "Calm", "Playful",
                "Mysterious", "Professional", "Bold"
        });

        fullBodySwitch = new Switch(this);
        fullBodySwitch.setText("Full-body composition");
        fullBodySwitch.setTextColor(TEXT);
        fullBodySwitch.setTextSize(15);
        fullBodySwitch.setChecked(true);
        fullBodySwitch.setButtonTintList(null);
        LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switchParams.topMargin = dp(8);
        appearanceCard.addView(fullBodySwitch, switchParams);

        LinearLayout sceneCard = card("03  SCENE & STYLE");
        root.addView(sceneCard, cardParams());

        outfitSpinner = addSpinner(sceneCard, "Outfit", new String[]{
                "Minimal streetwear", "Smart casual", "Luxury fashion", "Formal suit", "Kerala traditional saree",
                "Kerala traditional mundu outfit", "Sportswear", "Casual t-shirt and jeans",
                "Futuristic outfit", "Fantasy costume"
        });
        environmentSpinner = addSpinner(sceneCard, "Environment", new String[]{
                "Minimalist interior", "Clean studio", "Modern apartment", "Urban street",
                "Natural outdoor landscape", "Luxury hotel", "Coffee shop", "Beach",
                "Futuristic city", "Fantasy environment"
        });
        lightingSpinner = addSpinner(sceneCard, "Lighting", new String[]{
                "Soft cinematic lighting", "Natural window light", "Golden hour sunlight",
                "Bright studio lighting", "Dramatic chiaroscuro", "Neon rim lighting",
                "Soft overcast daylight", "High-key commercial lighting"
        });
        lensSpinner = addSpinner(sceneCard, "Camera / lens", new String[]{
                "85mm portrait lens", "50mm natural perspective", "35mm environmental portrait",
                "24mm dynamic wide angle", "Smartphone portrait camera", "Telephoto editorial lens"
        });
        styleSpinner = addSpinner(sceneCard, "Visual style", new String[]{
                "Hyper-realistic photography", "Editorial fashion photography", "Cinematic photography",
                "Luxury commercial photography", "Natural smartphone photography",
                "3D animated character", "Anime illustration", "Graphic novel art"
        });
        ratioSpinner = addSpinner(sceneCard, "Aspect ratio", new String[]{
                "9:16 vertical", "4:5 portrait", "1:1 square", "3:4 portrait", "16:9 landscape"
        });

        extraInput = edit("Extra details — pose, accessories, skin, mood, colors...");
        extraInput.setMinLines(3);
        extraInput.setGravity(Gravity.TOP | Gravity.START);
        extraInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        sceneCard.addView(extraInput, fieldParams());

        LinearLayout actionRow = new LinearLayout(this);
        actionRow.setOrientation(LinearLayout.HORIZONTAL);
        actionRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams actionRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionRowParams.topMargin = dp(6);
        actionRowParams.bottomMargin = dp(18);
        root.addView(actionRow, actionRowParams);

        Button randomButton = secondaryButton("Randomize");
        randomButton.setOnClickListener(v -> randomize());
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0, dp(54), 1f);
        half.rightMargin = dp(8);
        actionRow.addView(randomButton, half);

        Button generateButton = primaryButton("Generate Prompt");
        generateButton.setOnClickListener(v -> generatePrompt());
        LinearLayout.LayoutParams grow = new LinearLayout.LayoutParams(0, dp(54), 1.35f);
        grow.leftMargin = dp(8);
        actionRow.addView(generateButton, grow);

        LinearLayout outputCard = card("04  GENERATED PROMPT");
        root.addView(outputCard, cardParams());

        outputView = text(
                "Your generated prompt will appear here. Choose the character details above and tap Generate Prompt.",
                15, TEXT, Typeface.NORMAL);
        outputView.setTextIsSelectable(true);
        outputView.setLineSpacing(0, 1.22f);
        outputView.setPadding(dp(14), dp(14), dp(14), dp(14));
        outputView.setBackground(rounded(CARD_2, 18, BORDER, 1));
        outputCard.addView(outputView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout outputActions = new LinearLayout(this);
        outputActions.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams outputActionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        outputActionParams.topMargin = dp(12);
        outputCard.addView(outputActions, outputActionParams);

        Button copy = secondaryButton("Copy");
        copy.setOnClickListener(v -> copyPrompt());
        LinearLayout.LayoutParams third = new LinearLayout.LayoutParams(0, dp(48), 1f);
        third.rightMargin = dp(6);
        outputActions.addView(copy, third);

        Button share = secondaryButton("Share");
        share.setOnClickListener(v -> sharePrompt());
        LinearLayout.LayoutParams third2 = new LinearLayout.LayoutParams(0, dp(48), 1f);
        third2.leftMargin = dp(6);
        third2.rightMargin = dp(6);
        outputActions.addView(share, third2);

        Button clear = secondaryButton("Clear");
        clear.setOnClickListener(v -> {
            outputView.setText("");
            prefs.edit().remove("last_prompt").apply();
        });
        LinearLayout.LayoutParams third3 = new LinearLayout.LayoutParams(0, dp(48), 1f);
        third3.leftMargin = dp(6);
        outputActions.addView(clear, third3);

        TextView footer = text("Built to stay lightweight • Offline prompt generation • v1.0.2", 12, MUTED, Typeface.NORMAL);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(18), 0, 0);
        root.addView(footer);

        return scrollView;
    }

    private LinearLayout card(String titleText) {
        LinearLayout card = column();
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(rounded(CARD, 24, BORDER, 1));

        TextView title = text(titleText, 13, ACCENT, Typeface.BOLD);
        title.setLetterSpacing(0.08f);
        title.setPadding(0, 0, 0, dp(12));
        card.addView(title);
        return card;
    }

    private Spinner addSpinner(LinearLayout parent, String label, String[] options) {
        TextView labelView = text(label, 13, MUTED, Typeface.BOLD);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        labelParams.topMargin = dp(4);
        parent.addView(labelView, labelParams);

        Spinner spinner = new Spinner(this, Spinner.MODE_DROPDOWN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parentView) {
                TextView view = (TextView) super.getView(position, convertView, parentView);
                styleSpinnerText(view);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parentView) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parentView);
                view.setTextColor(TEXT);
                view.setTextSize(15);
                view.setPadding(dp(14), dp(13), dp(14), dp(13));
                view.setBackgroundColor(CARD_2);
                return view;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setBackground(rounded(CARD_2, 16, BORDER, 1));
        spinner.setPadding(dp(8), 0, dp(8), 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(52));
        params.topMargin = dp(6);
        params.bottomMargin = dp(10);
        parent.addView(spinner, params);

        randomizable.add(spinner);
        return spinner;
    }

    private void styleSpinnerText(TextView view) {
        view.setTextColor(TEXT);
        view.setTextSize(15);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setPadding(dp(12), 0, dp(12), 0);
    }

    private EditText edit(String hint) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setHintTextColor(Color.rgb(111, 114, 128));
        editText.setTextColor(TEXT);
        editText.setTextSize(15);
        editText.setSingleLine(false);
        editText.setPadding(dp(14), dp(12), dp(14), dp(12));
        editText.setBackground(rounded(CARD_2, 16, BORDER, 1));
        return editText;
    }

    private void generatePrompt() {
        String name = nameInput.getText().toString().trim();
        String extra = extraInput.getText().toString().trim();

        StringBuilder prompt = new StringBuilder();

        if (!name.isEmpty()) {
            prompt.append(name).append(", ");
        }

        prompt.append(value(ageSpinner)).append(", ")
                .append(value(ethnicitySpinner)).append(", ")
                .append(value(roleSpinner)).append(", ")
                .append(value(genderSpinner).toLowerCase(Locale.US)).append(" character, ")
                .append(value(buildSpinner).toLowerCase(Locale.US)).append(" build, ")
                .append(value(hairSpinner).toLowerCase(Locale.US)).append(" hairstyle in ")
                .append(value(hairColorSpinner).toLowerCase(Locale.US)).append(", ")
                .append(value(eyeSpinner)).append(" eyes, ")
                .append(value(expressionSpinner).toLowerCase(Locale.US)).append(" expression, ")
                .append(value(demeanorSpinner).toLowerCase(Locale.US)).append(" demeanor. ");

        prompt.append(fullBodySwitch.isChecked() ? "Full-body portrait, " : "Portrait composition, ")
                .append("hands naturally composed, realistic anatomy and proportions. ");

        prompt.append("Wearing ").append(value(outfitSpinner).toLowerCase(Locale.US))
                .append(", set against ").append(value(environmentSpinner).toLowerCase(Locale.US)).append(". ");

        prompt.append(value(styleSpinner)).append(", ")
                .append(value(lightingSpinner).toLowerCase(Locale.US)).append(", shot with ")
                .append(value(lensSpinner).toLowerCase(Locale.US))
                .append(", ultra-detailed skin, hair and fabric textures, realistic depth, clean composition, ")
                .append("professional color grading, natural shadows, high dynamic range, premium production quality.");

        if (!extra.isEmpty()) {
            prompt.append(" Additional direction: ").append(extra);
            if (!extra.endsWith(".") && !extra.endsWith("!")) {
                prompt.append(".");
            }
        }

        prompt.append(" --ar ").append(aspectCode(value(ratioSpinner)));

        String result = prompt.toString();
        outputView.setText(result);
        prefs.edit().putString("last_prompt", result).apply();
        Toast.makeText(this, "Prompt generated", Toast.LENGTH_SHORT).show();
    }

    private String aspectCode(String value) {
        if (value.startsWith("4:5")) return "4:5";
        if (value.startsWith("1:1")) return "1:1";
        if (value.startsWith("3:4")) return "3:4";
        if (value.startsWith("16:9")) return "16:9";
        return "9:16";
    }

    private void randomize() {
        for (Spinner spinner : randomizable) {
            if (spinner.getCount() > 0) {
                spinner.setSelection(random.nextInt(spinner.getCount()));
            }
        }
        fullBodySwitch.setChecked(random.nextBoolean());
        Toast.makeText(this, "Character randomized", Toast.LENGTH_SHORT).show();
    }

    private void copyPrompt() {
        String prompt = outputView.getText().toString().trim();
        if (prompt.isEmpty()) {
            Toast.makeText(this, "Generate a prompt first", Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("Character prompt", prompt));
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void sharePrompt() {
        String prompt = outputView.getText().toString().trim();
        if (prompt.isEmpty()) {
            Toast.makeText(this, "Generate a prompt first", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, prompt);
        startActivity(Intent.createChooser(intent, "Share character prompt"));
    }

    private String value(Spinner spinner) {
        Object item = spinner.getSelectedItem();
        return item == null ? "" : item.toString();
    }

    private LinearLayout column() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        return layout;
    }

    private TextView text(String value, int sizeSp, int color, int style) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(sizeSp);
        view.setTextColor(color);
        view.setTypeface(Typeface.create("sans", style));
        return view;
    }

    private Button primaryButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextColor(Color.WHITE);
        button.setTextSize(14);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        GradientDrawable background = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{ACCENT, Color.rgb(102, 110, 255)}
        );
        background.setCornerRadius(dp(18));
        button.setBackground(background);
        return button;
    }

    private Button secondaryButton(String label) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextColor(TEXT);
        button.setTextSize(14);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setBackground(rounded(CARD_2, 18, BORDER, 1));
        return button;
    }

    private GradientDrawable rounded(int fill, float radiusDp, int strokeColor, int strokeDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fill);
        drawable.setCornerRadius(dp(radiusDp));
        drawable.setStroke(dp(strokeDp), strokeColor);
        return drawable;
    }

    private LinearLayout.LayoutParams cardParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp(14);
        return params;
    }

    private LinearLayout.LayoutParams fieldParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = dp(12);
        return params;
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int dp(float value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
