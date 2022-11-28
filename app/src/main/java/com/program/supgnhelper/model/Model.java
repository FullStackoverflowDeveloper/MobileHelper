package com.program.supgnhelper.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.presenters.AboutPresenter;
import com.program.supgnhelper.presenters.AddressPresenter;
import com.program.supgnhelper.presenters.CatalogPresenter;
import com.program.supgnhelper.presenters.EditPresenter;
import com.program.supgnhelper.presenters.ListMasterPresenter;
import com.program.supgnhelper.presenters.LoginPresenter;
import com.program.supgnhelper.presenters.ProfilePresenter;
import com.program.supgnhelper.presenters.ResultPresenter;
import com.program.supgnhelper.presenters.TaskPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Model {
    private final DatabaseReference userDb;
    private final DatabaseReference taskDb;
    private final DatabaseReference addressDb;
    private final DatabaseReference descriptionDb;
    private static final String TAG = "DATABASE";

    public Model(){
        DatabaseReference root = FirebaseDatabase.getInstance("https://mobilehelper-fd39a-default-rtdb.firebaseio.com").getReference();
        userDb = root.child("users");
        taskDb = root.child("items");
        addressDb = root.child("streets");
        descriptionDb = root.child("title");
    }

    // Добавление района
    public void addLocal(Local local){
        addressDb.child(String.valueOf(local.getId())).setValue(local);
    }

    // Получение описания
    public void getDescription(AboutPresenter presenter){
        descriptionDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    if (Objects.equals(data.getKey(), "description")){
                        presenter.titleLoaded(Objects.requireNonNull(data.getValue()).toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Получение адреса
    public void getAddress(EditPresenter presenter){
        addressDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<Local> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Local local = snap.getValue(Local.class);
                    data.add(local);
                }
                presenter.addressLoad(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                presenter.errorPr("");
                log(error.getMessage());
            }
        });
    }

    // Получение адреса
    public void getAddress(AddressPresenter presenter){
        addressDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<Local> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Local local = snap.getValue(Local.class);
                    data.add(local);
                }
                presenter.addressLoaded(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log(error.getMessage());
            }
        });
    }

    // Обновление адреса
    public void updateAddress(Local local){
        addressDb.child(String.valueOf(local.getId())).updateChildren(local.generateMap());
    }

    // Добавление пользователя
    public void addUser(User user){
        userDb.push().setValue(user);
    }

    // Проверка существования пользователя
    public void checkUser(String email, String password, LoginPresenter presenter){
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                User currentUser = null;
                for(DataSnapshot snap: snapshot.getChildren()){
                    User user = snap.getValue(User.class);
                    assert user != null;
                    if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                        currentUser = user;
                    }
                }
                if (currentUser != null){
                    presenter.userFound(currentUser.getRole(), currentUser.getId());
                }else {
                    presenter.errorPr();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                presenter.errorPr();
                log(error.getMessage());
            }
        });
    }

    // Получение всех работников
    public void getWorkers(ListMasterPresenter presenter){
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<User> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    User user = snap.getValue(User.class);
                    assert user != null;
                    if (user.getRole() == Role.WORKER) {
                        data.add(user);
                    }
                }
                presenter.dataLoadedPr(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to read users");
                presenter.showMsgPr("Failed to read users");
            }
        });
    }

    private static void log(String msg){
        Log.d(TAG, msg);
    }

    // Добавление задач
    public void addTask(Item item){
        taskDb.child(String.valueOf(item.getId())).setValue(item);
    }

    public void getItem(int id, ResultPresenter presenter){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    assert item != null;
                    if(item.getId() == id){
                        presenter.itemLoaded(item);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get tasks");
                presenter.errorPr("Failed to get tasks");
            }
        });
    }

    // Получение всех элементов по адресу
    public void getItemsByAddress(CatalogPresenter presenter, int id){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    assert item != null;
                    if(item.getAddressId() == id){
                        data.add(item);
                    }
                }
                presenter.itemsLoaded(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get tasks");
            }
        });
    }

    // Получение пользователя по id
    public void getMaster(int id, ProfilePresenter presenter) {
        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    assert user != null;
                    if (user.getId() == id) {
                        presenter.loaded(user);
                        break;
                    }
                }
                log("Failed to get master's name");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get master's name");
            }
        });
    }

    // Получение элемента по id адреса
    public void getItemByAddress(int id, EditPresenter presenter){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    assert item != null;
                    if(item.getAddressId() == id){
                        presenter.itemLoaded(item);
                        break;
                    }
                }
                presenter.errorPr("Item not found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get tasks");
                presenter.errorPr("Failed to get tasks");
            }
        });
    }


    // Получение элемента
    public void getItem(int id, EditPresenter presenter){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    assert item != null;
                    if(item.getId() == id){
                        presenter.itemLoaded(item);
                        break;
                    }
                }
                presenter.errorPr("Item not found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get tasks");
                presenter.errorPr("Failed to get tasks");
            }
        });
    }

    // Поиск по номеру счетчика
    public void searchItems(TaskPresenter presenter, String query){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<Item> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    assert item != null;
                    try {

                        if (item.getNumberCounter() == Integer.parseInt(query)) {
                            data.add(item);
                        }
                    }catch (NumberFormatException ex){
                        presenter.showMsgPr("Wrong format");
                    }
                }
                presenter.dataLoadedPr(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                presenter.showMsgPr(error.getMessage());
            }
        });
    }

    // Загрузка задач
    public void getTasks(TaskPresenter presenter, int worker){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<Item> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    assert item != null;
                    if (item.getWorkerId() == worker) {
                        data.add(item);
                    }
                }
                presenter.dataLoadedPr(filterItems(data, Status.TASK));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get tasks");
                presenter.showMsgPr("Failed to get tasks");
            }
        });
    }

    // Загрузка всех элементов
    public void loadAllItems(TaskPresenter presenter){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<Item> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    data.add(snap.getValue(Item.class));
                }
                presenter.dataLoadedPr(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get tasks");
                presenter.showMsgPr("Failed to get tasks");
            }
        });
    }

    // Загрузка всех элементов
    public void loadAllItems(CatalogPresenter presenter){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<Item> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    data.add(snap.getValue(Item.class));
                }
                presenter.itemsLoaded(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get tasks");
            }
        });
    }

    // Получение заявок
    public void getWorks(TaskPresenter presenter, int worker){
        taskDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                log("Value is " + snapshot);
                List<Item> data = new ArrayList<>();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Item item = snap.getValue(Item.class);
                    assert item != null;
                    if (item.getWorkerId() == worker){
                        data.add(item);
                    }
                }
                presenter.dataLoadedPr(filterItems(data, Status.WORK));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                log("Failed to get works");
                presenter.showMsgPr("Failed to get works");
            }
        });
    }

    // Обновление элемента
    public void updateItem(Item item){
        taskDb.child(String.valueOf(item.getId())).updateChildren(item.generateMap());
    }

    // Алгоритм фильтрации по статусу
    public static List<Item> filterItems(List<Item> list, Status status){
        List<Item> data = new ArrayList<>();
        for (Item item: list){
            if (item.getStatus() != status){
                continue;
            }
            data.add(item);
        }

        return data;
    }

    // Генерация id
    public static int generateId(){
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    // Получение строки адреса
    public static String getAddress(List<Local> locals, int id){
        String localName;
        String streetName;
        String homeName;
        String flatName = null;
        for (Local local: locals){
            localName = local.getName();
            for (Street street: local.getStreets()){
                streetName = street.getName();
                for(Home home: street.getHomes()){
                    homeName = String.valueOf(home.getNumber());
                    if (id == home.getId()){
                        return localName + ", " + streetName + ", дом " + homeName;
                    }
                    if (home.isFlat()){
                        for (Flat flat: home.getFlats()){
                            if (id == flat.getId()){
                                flatName = String.valueOf(flat.getNumber());
                                return localName + ", " + streetName + ", дом " + homeName + ", квартира " + flatName;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
