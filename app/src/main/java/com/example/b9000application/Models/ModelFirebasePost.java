package com.example.b9000application.Models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.b9000application.Models.Entities.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;

public class ModelFirebasePost extends ModelFirebase {
    final public static ModelFirebasePost instance = new ModelFirebasePost();



    public ListenerRegistration getGetPostsPerUserListener() {
        return getPostsPerUserListener;
    }

    public void setGetPostsPerUserListener(ListenerRegistration getPostsPerUserListener) {
        this.getPostsPerUserListener = getPostsPerUserListener;
    }

    private ListenerRegistration getPostsPerUserListener;
    public static ModelFirebasePost getInstance() {
        return instance;
    }

    private ModelFirebasePost() {
        super();


    }

    void GetAllPosts(final PostRepository.GetAllPostsListener listener) {
//        if (isNetworkConnected()) {
//            db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                    if (e != null) {
//                        Log.d("Firestore getall Error", "Error:" + e.getMessage());
//                        LinkedList<Post> data = new LinkedList<>();
//                        listener.onResponse(data);
//                    } else {
//                        db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).whereEqualTo("deleted", 0).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                if (!isNetworkConnected()) {
//                                    listener.onError();
//                                } else {
//                                    final LinkedList<Post> data = new LinkedList<>();
//                                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments())
//                                    {
//                                        Post post = doc.toObject(Post.class);
//                                        data.add(post);
//                                    }
//                                    Log.d("FIrebase", "onEvent: firebase getall posts"+ " "+ data.size());
//                                    listener.onResponse(data);
//
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                listener.onError();
//                            }
//                        });
//
//                    }
//                }
//            });
//        }
//        else
//        {
//            listener.onError();
//        }
//        db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                LinkedList<Post> data = new LinkedList<>();
//                if (e != null) {
//                    listener.onResponse(data);
//                    return;
//                }
//                db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).whereEqualTo("deleted", 0).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        LinkedList<Post> data = new LinkedList<>();
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            Post post = doc.toObject(Post.class);
//                            data.add(post);
//                        }
//                        listener.onResponse(data);
//                    }
//                });
//            }
//        });
//        db.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                LinkedList<Post> data = new LinkedList<>();
//                if (e != null) {
//                    listener.onResponse(data);
//                    return;
//                }
//                db.collection("Posts").orderBy("timestamp", Query.Direction.ASCENDING).whereEqualTo("deleted", 0).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    LinkedList<Post> data = new LinkedList<>();
//
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                            Post post = doc.toObject(Post.class);
//                            data.add(post);
//                        }
//                        listener.onResponse(data);
//                    }
//                });
//            }
//        });

        db.collection("Posts").whereEqualTo("deleted", 0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                LinkedList<Post> data = new LinkedList<>();
                if (e != null) {
                    listener.onResponse(data);
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Post post = doc.toObject(Post.class);
                    data.add(post);
                }
                listener.onResponse(data);
            }
        });
    }


    void addPost(Post p, final PostRepository.InsertPostListener listener) {
        if (isNetworkConnected())
        {
            String userId = p.getUserId();
            final DocumentReference doc = db.collection("Posts").document();
            String key = doc.getId();
            p.setPostKey(key);
            doc.collection("Comments");
            doc.set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    listener.onComplete(task.isSuccessful());

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError(e);

                }
            });
        }
        else
        {
            listener.onOffline();
        }

    }


    void updatePost(Post p, final PostRepository.InsertPostListener listener) {
        if(!isNetworkConnected())
        {
            listener.onOffline();
        }
        else
        {
            String userId = p.getUserId();
            db.collection("Posts").document(p.getPostKey()).set(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listener.onComplete(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError(e);
                }
            });
        }

    }



     void getPost(String postKey, final PostRepository.GetPostListener listener) {
        if (isNetworkConnected())
        {
            db.collection("Posts").document(postKey).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if (e!=null){
                        Log.d("Firestore","Error:"+e.getMessage());
                    }
                    else
                    {

                        final Post p = documentSnapshot.toObject(Post.class);
                        listener.onResponse(p);

                    }
                }

            });
        }
        else {
            listener.onError();



        }}



    public void saveBlogImage(Uri pickerImgUri, final PostRepository.SaveImageListener listener) {
        if (isNetworkConnected())
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
            final StorageReference imageFilePath = storageReference.child(pickerImgUri.getLastPathSegment());
            imageFilePath.putFile(pickerImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.onComplete(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onError(e);
                        }
                    });
                }
            });
        }
        else
        {
            listener.onOffline();
        }
    }


    public void ifPostExists(String postKey, final PostRepository.ExistPostListener listener) {
        if (isNetworkConnected())
        {
            db.collection("Posts").whereEqualTo("deleted",0).whereEqualTo("postKey",postKey).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.size() == 0)
                    {
                        listener.onNotExist();
                    }
                    else{
                        listener.onExist();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onError(e);
                }
            });
        }
       else
        {
            listener.onOffline();
        }


    }


    public void activatePostsPerUserListener(final String userId, final PostRepository.GetAllPostsListener listener) {
        this.setGetPostsPerUserListener(db.collection("Posts").whereEqualTo("userId", userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.e("Firestore","Error:"+e.getMessage());
                }
                else
                {
                    db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).whereEqualTo("deleted", 0).whereEqualTo("userId", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.d("FIrebase", "onEvent: firebase get posts per user");
                            if (!isNetworkConnected())
                            {
                                listener.onError();
                            }
                            else {
                                final LinkedList<Post> data = new LinkedList<>();
                                for (DocumentSnapshot doc : queryDocumentSnapshots
                                ) {
                                    Post p = doc.toObject(Post.class);
                                    data.add(p);

                                }
                                listener.onResponse(data);

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onError();
                        }
                    });

                }
            }
        }));
    }

    public void removePostsPerUserListener() {
        if(this.getPostsPerUserListener!=null)
            this.getPostsPerUserListener.remove();
    }

}
