package by.grodno.vasili.data.util;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposables;

public class SingleValueOnSubscribe implements SingleOnSubscribe<DataSnapshot> {
    private Query query;

    public SingleValueOnSubscribe(Query query) {
        this.query = query;
    }

    @Override
    public void subscribe(SingleEmitter<DataSnapshot> emitter) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (!emitter.isDisposed()) {
                    emitter.onError(databaseError.toException());
                }
            }
        };

        emitter.setDisposable(Disposables.fromRunnable(() -> {
            if (query != null) {
                query.removeEventListener(listener);
                query = null;
            }
        }));

        if (query != null) {
            query.addListenerForSingleValueEvent(listener);
        }
    }
}
