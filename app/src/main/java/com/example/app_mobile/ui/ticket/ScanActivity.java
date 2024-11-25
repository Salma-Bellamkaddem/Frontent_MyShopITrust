package com.example.app_mobile.ui.ticket;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_mobile.Apis.TicketApi;
import com.example.app_mobile.Entities.Produit;
import com.example.app_mobile.Entities.Ticket;
import com.example.app_mobile.R;

import com.example.app_mobile.retrofit.RetrofitService;
import com.example.app_mobile.ui.user.LoginActivity;
import com.example.app_mobile.ui.user.SignUpActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActivity";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;


    private TextRecognizer textRecognizer;
    private ProgressDialog progressDialog;
    private Uri imageUrl = null;
    private MaterialButton inputImageBtn, recognizeTextBtn;
    private ShapeableImageView imageIv;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private ImageView backButton;

    private ImageView validationIcon;
    private TextView nameTextView;
    private TextView priceTextView;
    private TicketViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_scan);
        // Initialize UI components
        validationIcon = findViewById(R.id.validation_icon);
        backButton = findViewById(R.id.backButton);
        // Initialize Database
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Initialize TextRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        backButton = findViewById(R.id.backButton);
        nameTextView = findViewById(R.id.nameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        viewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        // Set up listeners
        backButton.setOnClickListener(v -> startActivity(new Intent(ScanActivity.this, TicketDetailsActivity.class)));
        // Initialize UI components
        inputImageBtn = findViewById(R.id.inputImageBtn);
        recognizeTextBtn = findViewById(R.id.recofnizeTextBtn);
        imageIv = findViewById(R.id.imageIv);
        nameTextView = findViewById(R.id.nameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        inputImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageCamera();
            }
        });
        recognizeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrl == null) {
                    Toast.makeText(ScanActivity.this, "Pick image first", Toast.LENGTH_SHORT).show();
                } else {
                    recognizeTextFromImage();
                }
            }
        });
        // Définir l'action du bouton de retour
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer l'ID du ticket depuis l'Intent (ou depuis une autre source dans cette activité)
                Long ticketId = getIntent().getLongExtra("ticketId", -1);

                if (ticketId != -1) {
                    // Créer un Intent pour DetailsTicketActivity
                    Intent intent = new Intent(ScanActivity.this, TicketDetailsActivity.class);
                    // Passer le ticketId à DetailsTicketActivity
                    intent.putExtra("ticketId", ticketId);
                    // Démarrer DetailsTicketActivity
                    startActivity(intent);
                } else {
                    // Gérer le cas où ticketId est invalide ou non disponible
                    Log.e("ScanActivity", "Invalid or missing ticket ID");
                    Toast.makeText(ScanActivity.this, "Invalid ticket ID", Toast.LENGTH_SHORT).show();
                }
            }
        });




        validationIcon.setOnClickListener(v -> {
            // Récupérer le nom et le prix du produit à partir des TextViews
            String productName = nameTextView.getText().toString().trim();
            String productPriceText = priceTextView.getText().toString().trim();
            double productPrice;

            Log.d(TAG, "Product Name: " + productName);
            Log.d(TAG, "Product Price Text: " + productPriceText);

            // Convertir le prix du produit en double
            try {
                productPrice = Double.parseDouble(productPriceText.replaceAll("[^\\d.,]", "").replace(',', '.'));
                Log.d(TAG, "Product Price Parsed: " + productPrice);
            } catch (NumberFormatException e) {
                // Gérer les erreurs de format de prix
                Log.e(TAG, "Invalid price format: " + productPriceText, e);
                Toast.makeText(ScanActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                return; // Arrêter l'exécution si le format est invalide
            }

            Intent intent = getIntent();
            Bundle ticket = intent.getExtras();
            Log.e("ticketRetrieved", "ticket"+ticket);
            // Obtenir l'ID du ticket depuis les extras de l'Intent
            Long ticketId = getIntent().getLongExtra("ticketId", -1);
            Log.d(TAG, "Ticket ID Retrieved: " + ticketId);

            // Vérifier la validité de l'ID du ticket
            if (ticketId == -1) {
                Log.e(TAG, "Invalid ticket ID: " + ticketId);
                Toast.makeText(ScanActivity.this, "Invalid ticket ID", Toast.LENGTH_SHORT).show();
                return; // Arrêter l'exécution si l'ID du ticket est invalide
            }

            // Ajouter le produit au ticket via l'API
            try {
                Log.d(TAG, "Adding product to ticket with ID: " + ticketId);
                addProductToTicket(ticketId, productName, productPrice);
                Log.d(TAG, "Product added successfully: " + productName + " with price: " + productPrice);
            } catch (Exception e) {
                // Gérer les erreurs lors de l'ajout du produit au ticket
                Log.e(TAG, "Error adding product to ticket: " + productName + " with price: " + productPrice, e);
                Toast.makeText(ScanActivity.this, "Failed to add product to ticket", Toast.LENGTH_SHORT).show();
                return; // Arrêter l'exécution si l'ajout échoue
            }

            // Préparer l'intent de résultat pour retourner les données du produit
            Intent resultIntent = new Intent();
            resultIntent.putExtra("productName", productName);
            resultIntent.putExtra("productPrice", productPrice);

            Log.d(TAG, "Returning result with Product Name: " + productName + " and Product Price: " + productPrice);

            // Renvoyer le résultat à l'activité précédente
            setResult(RESULT_OK, resultIntent);

            // Fermer l'activité actuelle et revenir à l'activité précédente
            Log.d(TAG, "Finishing ScanActivity");
            finish();
        });


    }



    // Méthode pour ajouter un produit au ticket via l'API
    private void addProductToTicket(Long ticketId, String productName, double productPrice) {
        // Créer un objet Produit
        Produit produit = new Produit();
        produit.setNom(productName);
        produit.setPrice(productPrice);

        Log.d("AddProductToTicket", "Creating product with Name: " + productName + " and Price: " + productPrice);

        // Utiliser Retrofit pour appeler l'API et ajouter le produit au ticket
        RetrofitService retrofitService = new RetrofitService();
        TicketApi ticketApi = retrofitService.getRetrofit().create(TicketApi.class);
        Call<Ticket> call = ticketApi.addProductToTicket(ticketId, produit);

        Log.d("AddProductToTicket", "Making API call to add product to ticket ID: " + ticketId);

        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                if (response.isSuccessful()) {
                    // Afficher un message de succès
                    Log.d("AddProductToTicket", "Product added successfully. Response code: " + response.code());
                    Toast.makeText(ScanActivity.this, "Produit ajouté au ticket", Toast.LENGTH_SHORT).show();

                    // Mettre à jour la liste des produits dans l'activité des détails du ticket
                    Intent intent = new Intent(ScanActivity.this, TicketDetailsActivity.class);
                    intent.putExtra("ticket_id", ticketId); // Passer l'ID du ticket à l'activité des détails
                    startActivity(intent); // Optionnellement utiliser startActivityForResult
                } else {
                    // Gérer les erreurs de réponse
                    Log.e("AddProductToTicket", "Failed to add product. Response code: " + response.code());
                    try {
                        Log.e("AddProductToTicket", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("AddProductToTicket", "Error reading error body", e);
                    }
                    Toast.makeText(ScanActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                // Gérer les erreurs de requête
                Log.e("AddProductToTicket", "Failed to add product. Error message: " + t.getMessage(), t);
                Toast.makeText(ScanActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void recognizeTextFromImage() {
        if (textRecognizer == null || imageUrl == null) {
            progressDialog.dismiss();
            Log.e(TAG, "Text recognizer or image URL is null");
            Toast.makeText(this, "Error: Text recognizer or image URL is null", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUrl);
            textRecognizer.process(inputImage)
                    .addOnSuccessListener(text -> {
                        progressDialog.dismiss();
                        String recognizedText = text.getText();
                        if (recognizedText != null && !recognizedText.isEmpty()) {
                            String[] productDetails = extractProductDetails(recognizedText);
                            nameTextView.setText(productDetails[0]);
                            priceTextView.setText(productDetails[1]);
                        } else {
                            Toast.makeText(ScanActivity.this, "No text recognized", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: ", e);
                        Toast.makeText(ScanActivity.this, "Failed recognizing text due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } catch (IOException e) {
            progressDialog.dismiss();
            Log.d(TAG, "recognizeTextFromImage: ", e);
            Toast.makeText(this, "Failed preparing image due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String[] extractProductDetails(String recognizedText) {
        String[] details = new String[2];
        details[0] = ""; // Default product name
        details[1] = ""; // Default price

        Pattern namePattern = Pattern.compile("([A-Za-z\\s]+)");
        Pattern[] pricePatterns = {
                Pattern.compile("\\d{1,3}(?:[.,]\\d{3})*(?:[.,]\\d{2})?"),
                Pattern.compile("(\\d+(?:\\.\\d{1,2})?)"),
                Pattern.compile("\\d+(?:[.,]\\d{2})?\\s?(?:USD|TTC|[€$]|dollars)")
        };

        Matcher nameMatcher = namePattern.matcher(recognizedText);
        if (nameMatcher.find()) {
            details[0] = nameMatcher.group(1).trim();
        }

        for (Pattern pattern : pricePatterns) {
            Matcher priceMatcher = pattern.matcher(recognizedText);
            if (priceMatcher.find()) {
                details[1] = priceMatcher.group().trim();
                break;
            }
        }

        return details;
    }

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");
        imageUrl = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    imageIv.setImageURI(imageUrl);
                } else {
                    Toast.makeText(ScanActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                pickImageCamera();
            } else {
                Toast.makeText(this, "Camera & Storage permissions are required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
