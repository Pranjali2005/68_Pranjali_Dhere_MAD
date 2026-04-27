package com.example.final_project.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.*;
import android.widget.*;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.activities.AdminActivity;
import com.example.final_project.activities.AuthorityActivity;
import com.example.final_project.database.DBHelper;
import com.example.final_project.model.ComplaintModel;

import java.io.File;
import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.VH> {

    Context context;
    List<ComplaintModel> list;
    boolean isAdmin, isAuthority;
    DBHelper db;
    AdminActivity adminActivity;

    public ComplaintAdapter(Context c, List<ComplaintModel> l,
                            boolean isAdmin, boolean isAuthority,
                            DBHelper db, AdminActivity adminActivity) {
        this.context = c;
        this.list = l;
        this.isAdmin = isAdmin;
        this.isAuthority = isAuthority;
        this.db = db;
        this.adminActivity = adminActivity;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView desc, status, date, proofLabel;
        ImageView image, proof;
        Button btnAction, btnReject;

        VH(View v) {
            super(v);
            desc = v.findViewById(R.id.desc);
            status = v.findViewById(R.id.status);
            date = v.findViewById(R.id.date);
            image = v.findViewById(R.id.image);
            proof = v.findViewById(R.id.proofImage);
            proofLabel = v.findViewById(R.id.proofLabel);
            btnAction = v.findViewById(R.id.resolve);
            btnReject = v.findViewById(R.id.reject);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context)
                .inflate(R.layout.item_complaint, parent, false));
    }

    @Override
    public void onBindViewHolder(VH h, int i) {
        ComplaintModel m = list.get(i);

        h.desc.setText(m.getDescription());
        h.date.setText(m.getDate());

        String status = (m.getStatus() == null) ? "Pending" : m.getStatus();

        // STATUS DISPLAY
        switch (status.toLowerCase()) {
            case "resolved":
                h.status.setText("✅ Resolved");
                h.status.setTextColor(Color.parseColor("#10B981"));
                break;
            case "rejected":
                h.status.setText("❌ Rejected");
                h.status.setTextColor(Color.parseColor("#EF4444"));
                break;
            case "in review":
                h.status.setText("🔍 In Review");
                h.status.setTextColor(Color.parseColor("#F59E0B"));
                break;
            default:
                h.status.setText("⏳ Pending");
                h.status.setTextColor(Color.parseColor("#94A3B8"));
        }

        // IMAGES
        loadImageSafely(h.image, m.getImage());

        boolean hasProof = m.getProofImage() != null && !m.getProofImage().isEmpty();
        if (hasProof) {
            boolean loaded = loadImageSafely(h.proof, m.getProofImage());
            h.proof.setVisibility(loaded ? View.VISIBLE : View.GONE);
            if (h.proofLabel != null)
                h.proofLabel.setVisibility(loaded ? View.VISIBLE : View.GONE);
            if (!loaded) hasProof = false;
        } else {
            h.proof.setVisibility(View.GONE);
            if (h.proofLabel != null) h.proofLabel.setVisibility(View.GONE);
        }

        // RESET BUTTONS
        h.btnAction.setVisibility(View.GONE);
        h.btnReject.setVisibility(View.GONE);
        h.btnAction.setEnabled(true);

        boolean isFinished = status.equalsIgnoreCase("resolved")
                || status.equalsIgnoreCase("rejected");

        // AUTHORITY
        if (isAuthority && !isFinished) {
            h.btnAction.setVisibility(View.VISIBLE);
            if (hasProof) {
                h.btnAction.setText("✅ Proof Uploaded");
                h.btnAction.setEnabled(false);
                h.btnAction.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#334155")));
            } else {
                h.btnAction.setText("📷 Upload Proof");
                h.btnAction.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#6366F1")));
                h.btnAction.setOnClickListener(v -> {
                    if (context instanceof AuthorityActivity)
                        ((AuthorityActivity) context).startProofUpload(m.getId());
                });
            }
        }

        // ADMIN
        else if (isAdmin && !isFinished) {
            h.btnReject.setVisibility(View.VISIBLE);
            h.btnReject.setOnClickListener(v -> {
                db.rejectComplaint(m.getId());
                m.setStatus("Rejected");
                notifyItemChanged(i);
            });

            h.btnAction.setVisibility(View.VISIBLE);
            if (hasProof) {
                h.btnAction.setText("✅ Resolve");
                h.btnAction.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#10B981")));
                h.btnAction.setOnClickListener(v -> {
                    db.markResolved(m.getId());
                    m.setStatus("Resolved");
                    notifyItemChanged(i);
                    // SEND NOTIFICATION
                    if (adminActivity != null)
                        adminActivity.sendResolvedNotification(m.getEmail());
                });
            } else {
                h.btnAction.setText("⏳ Awaiting Proof");
                h.btnAction.setEnabled(false);
                h.btnAction.setBackgroundTintList(
                        ColorStateList.valueOf(Color.parseColor("#334155")));
            }
        }
    }

    private boolean loadImageSafely(ImageView iv, String path) {
        if (path == null || path.isEmpty()) return false;
        try {
            if (path.startsWith("content://")) {
                iv.setImageResource(android.R.drawable.ic_menu_gallery);
                return true;
            }
            File f = new File(path);
            if (f.exists()) {
                iv.setImageURI(Uri.fromFile(f));
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        iv.setImageResource(android.R.drawable.ic_menu_gallery);
        return false;
    }

    @Override
    public int getItemCount() { return list.size(); }
}