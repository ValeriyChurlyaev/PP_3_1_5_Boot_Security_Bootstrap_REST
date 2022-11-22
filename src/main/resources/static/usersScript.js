// get authenticated User

async function auth() {
    let res = await fetch('http://localhost:8080/admin/api/auth');
    return await res.json();
}

authUserInfo();

currentUserTable();

allUsersTable();

modalEdit();

modalDelete();

newUser();


// authenticated User email/role

async function authUserInfo() {
    let user = await auth();
    document.getElementById("adminEmail").textContent = user.email;
    let roles = "";
    user.roles.forEach(role => {
        roles += role.name.substring(5, role.name.length) + " ";
    })
    document.getElementById("adminRole").textContent = roles;
}

//  Current User table

async function currentUserTable() {
    const tbody = document.querySelector('#UserAdminBody');

    let user = await auth();
    let roles = user.roles.map(role => role.name.substring(5, role.name.length));
    let tableRoles = '';
    roles.forEach(role => {tableRoles += `<div>${role}</div>`});

    tbody.innerHTML =
        `<tr>
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.firstName}</td>
            <td class="align-middle">${user.lastName}</td>
            <td class="align-middle">${user.age}</td>
            <td class="align-middle">${user.email}</td>
            <td class="align-middle">${tableRoles}</td>
            </tr>`;
}

//  all Users table

async function allUsersTable() {
    let response = await fetch("http://localhost:8080/admin/api/users");
    let users = await response.json();
    document.querySelector('#allUsersBody').innerHTML = '';
    users.forEach(user => {
        let table = "";
        let roles = user.roles.map(role => role.name.substring(5, role.name.length));
        let rolesInTable = '';
        roles.forEach(role => {rolesInTable += `<div>${role}</div>`});
        table +=
            `<tr id="tr${user.id}">
            <td class="align-middle">${user.id}</td>
            <td class="align-middle">${user.firstName}</td>
            <td class="align-middle">${user.lastName}</td>
            <td class="align-middle">${user.age}</td>           
            <td class="align-middle">${user.email}</td>
            <td class="align-middle">${rolesInTable}</td>
            <td class="align-middle"><button class="btn btn-primary btn-sm editBtn" data-bs-toggle="modal" data-bs-target="#editModal">Edit</button></td>
            <td class="align-middle"><button class="btn btn-danger btn-sm deleteBtn" data-bs-toggle="modal" data-bs-target="#deleteModal">Delete</button></td>
            </tr>`;
        document.querySelector('#allUsersBody').innerHTML += table;
    });
    document.querySelectorAll('.editBtn').forEach(btn => {
        onEditButton(btn);
    });
    document.querySelectorAll('.deleteBtn').forEach(btn => {
        onDeleteButton(btn);
    })
    await authUserInfo();
    await currentUserTable();
}

//  NEW USER

async function newUser() {

    let roles = await fetch('http://localhost:8080/admin/api/roles');
    roles = await roles.json();
    roles.forEach(role => {
        if (document.querySelector('#roles').children.length < 3) {
            let option = document.createElement("option");
            option.value = role.id;
            option.text = role.name.substring(5, role.name.length);
            document.querySelector('#roles').appendChild(option);
        }
    });

    document.querySelector('#newUserAddForm').addEventListener('submit', async (event) => {
        event.preventDefault();
        const url = 'http://localhost:8080/admin/api/newUser';
        let response = await fetch(url,{
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: document.querySelector('#firstName').value,
                lastName: document.querySelector('#lastName').value,
                age: document.querySelector('#age').value,
                email: document.querySelector('#email').value,
                password: document.querySelector('#password').value,
                roles: listOfRoles(document.querySelector('#roles'))
            })
        });
        await allUsersTable();
        document.querySelector('#newUserAddForm').reset();
        document.querySelector('a.newUser').classList.remove('active');
        document.querySelector('a.allUsersTable').classList.add('active');
        document.querySelector('#newUser').classList.remove('active');
        document.querySelector('#allUsersTable').classList.add('active');
    })
}

//  EDIT btn

function onEditButton(button) {
    button.addEventListener('click', (event) => {
        event.preventDefault();
        const tr = button.parentNode.parentNode;
        document.querySelector('#editId').value = tr.children[0].innerHTML;
        document.querySelector('#editFirstName').value = tr.children[1].innerHTML;
        document.querySelector('#editLastName').value = tr.children[2].innerHTML;
        document.querySelector('#editAge').value = tr.children[3].innerHTML;
        document.querySelector('#editEmail').value = tr.children[4].innerHTML;
        document.querySelector('#editPassword').value = '';

        document.querySelector('#editModalForm').ariaModal = 'show';
    })
}

//  DELETE btn

function onDeleteButton(button) {
    button.addEventListener('click', (event) => {
        event.preventDefault();
        document.querySelector('#deleteRoles').innerHTML = '';

        const tr = button.parentNode.parentNode;
        document.querySelector('#deleteId').value = tr.children[0].innerHTML;
        document.querySelector('#deleteFirstName').value = tr.children[2].innerHTML;
        document.querySelector('#deleteLastName').value = tr.children[2].innerHTML;
        document.querySelector('#deleteAge').value = tr.children[3].innerHTML;
        document.querySelector('#deleteEmail').value = tr.children[4].innerHTML;

        let roles = Array.from(tr.children[5].children).map(role => role.innerHTML);
        roles.forEach(role => {
            let option = document.createElement('option');
            option.text = role;
            document.querySelector('#deleteRoles').appendChild(option);
        })

        document.querySelector('#deleteModalForm').ariaModal = 'show';
    })
}

//  Modal EDIT

async function modalEdit() {
    let roles = await fetch("http://localhost:8080/admin/api/roles");
    roles = await roles.json();
    roles.forEach(role => {
        if (document.querySelector('#editRoles').children.length < 3) {
            let option = document.createElement("option");
            option.value = role.id;
            option.text = role.name.substring(5, role.name.length);
            document.querySelector('#editRoles').appendChild(option);
        }
    });

    document.querySelector('#editBtnSubmit').addEventListener('click', async (e) => {
        e.preventDefault();
        let url = `http://localhost:8080/admin/api/update/${document.querySelector('#editId').value}`;
        await fetch(url, {
            method: "PATCH",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: document.querySelector('#editId').value,
                firstName: document.querySelector('#editFirstName').value,
                lastName: document.querySelector('#editLastName').value,
                age: document.querySelector('#editAge').value,
                email: document.querySelector('#editEmail').value,
                password: document.querySelector('#editPassword').value,
                roles: listOfRoles(document.querySelector('#editRoles'))
            })
        });
        await allUsersTable();
        document.querySelector('#editModalForm').reset();
    });
}

//  Modal DELETE

function modalDelete() {
    document.querySelector('.deleteSubmit').addEventListener('click', async (event) => {
        event.preventDefault();
        let url = `http://localhost:8080/admin/api/delete/${document.querySelector('#deleteId').value}`;
        await fetch(url, {
            method: "DELETE"
        });
        await allUsersTable();
        document.querySelector('#deleteModalForm').reset();
    });
}

//  to Array roles

function listOfRoles(options) {
    let res = [];
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            res.push({id: options[i].value, role: options[i].text});
        }
    }
    return res;
}











