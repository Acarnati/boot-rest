$(async function () {
    await getTableWithUsers();
    addNewUserForm();
    mainTableWithUserId();
    getDefaultModal();
    addNewUser();
})

const roleJson = [];

fetch('/api/roles').then(function (response) {
    if (response.ok) {
        response.json().then(roleList => {
            roleList.forEach(role => {
                roleJson.push(role);
            });
        });
    } else {
        console.error('Network request for roles.json failed with response ' + response.status + ': ' + response.statusText);
    }
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    // bodyAdd : async function(user) {return {'method': 'POST', 'headers': this.head, 'body': user}},
    findAllUsers: async () => await fetch('api/users'),
    findUser: async () => await fetch('api/user'),
    findOneUser: async (id) => await fetch(`api/users/${id}`),
    addNewUser: async (user) => await fetch('api/users', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`api/users/${id}`, {
        method: 'PUT',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`api/users/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

async function mainTableWithUserId() {
    let table = $('#mainTableWithUserId tbody');
    table.empty();

    await userFetchService.findUser()
        .then(res => res.json())
        .then(user => {
            let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.login}</td>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>
                            <td>${user.password.slice(0, 15)}...</td>
                            <td>${user.age}</td>
                            <td>${user.roles.map(role => role.role)}</td>
                )`;
            table.append(tableFilling);
        })
}

async function getTableWithUsers() {
    let table = $('#mainTableWithUsers tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.login}</td>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>
                            <td>${user.password.slice(0, 15)}...</td>
                            <td>${user.age}</td>
                            <td>${user.roles.map(role => role.role)}</td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-info" 
                                data-toggle="modal" data-target="#someDefaultModal">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-target="#someDefaultModal">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })

    $("#mainTableWithUsers").find('button').on('click', (event) => {
        let defaultModal = $('#someDefaultModal');

        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}

async function addNewUser() {
    $('#addNewUserButton').on('click', async e => {
        let addUserForm = $('#defaultSomeForm')
        let login = addUserForm.find('#AddNewUserLogin').val().trim();
        let firstname = addUserForm.find('#AddNewUserFirstName').val().trim();
        let lastname = addUserForm.find('#AddNewUserLastName').val().trim();
        let age = addUserForm.find('#AddNewUserAge').val().trim();
        let password = addUserForm.find('#AddNewUserPassword').val().trim();
        let rolesArray = addUserForm.find('#newroles').val();
        let roles = [];

        for (let i = 0; i < rolesArray.length; i++) {
            roles.push(roleJson.filter(item => item.id == rolesArray[i]))
        }
        roles = [].concat.apply([], roles);

        let data = {
            login: login,
            firstname: firstname,
            lastname: lastname,
            password: password,
            age: age,
            roles: roles
        }

        const response = await userFetchService.addNewUser(data);
        if (response.ok) {
            getTableWithUsers();
            addUserForm.find('#AddNewUserLogin').val('');
            addUserForm.find('#AddNewUserFirstName').val('');
            addUserForm.find('#AddNewUserLastName').val('');
            addUserForm.find('#AddNewUserAge').val('');
            addUserForm.find('#AddNewUserPassword').val('');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert)
        }
    })
}

async function addNewUserForm() {
    let button = $(`#SliderNewUserForm`);
    let form = $(`#defaultSomeForm`)
    button.on('click', () => {
        if (form.attr("data-hidden") === "true") {
            form.attr('data-hidden', 'false');
            form.show();
            button.text('Hide panel');
        } else {
            form.attr('data-hidden', 'true');
            form.hide();
            button.text('Show panel');
        }
    });

    fetch('/api/roles').then(function (response) {
        if (response.ok) {
            form.find('#newroles').empty();
            response.json().then(roleList => {
                roleList.forEach(role => {
                    form.find('#newroles')
                        .append($('<option>').val(role.id).text(role.role));
                });
            });
        } else {
            console.error('Network request for roles.json failed with response ' + response.status + ': ' + response.statusText);
        }
    });
}

async function getDefaultModal() {
    $('#someDefaultModal').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}

async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();
    let modalForm = $(`#someDefaultModal`)

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-outline-success" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                <input type="text" class="form-control" id="id" name="id" value="${user.id}" disabled>
                <label th:for="login" class="col-form-label">E-mail</label>
                <input class="form-control" type="text" id="login" value="${user.login}">
                <label th:for="firstname" class="col-form-label">Name</label>
                <input class="form-control" type="firstname" id="firstname" value="${user.firstname}">
                <label th:for="lastname" class="col-form-label">Last name</label>
                <input class="form-control" type="lastname" id="lastname" value="${user.lastname}">
                <label th:for="password" class="col-form-label">Password</label>
                <input class="form-control" type="password" id="password" value="${user.password}">
                <label th:for="age" class="col-form-label">Age</label>
                <input class="form-control" id="age" type="number" value="${user.age}">
                <label th:for="rolesSelected" class="col-form-label">Roles</label>
                <select class="form-select w-100" name="rolesSelected" id="updateroles" multiple></select>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    });

    fetch('/api/roles').then(function (response) {
        if (response.ok) {
            modalForm.find('#updateroles').empty();
            response.json().then(roleList => {
                roleList.forEach(role => {
                    modalForm.find('#updateroles')
                        .append($('<option>').val(role.id).text(role.role));
                });
            });
        } else {
            console.error('Network request for roles.json failed with response ' + response.status + ': ' + response.statusText);
        }
    });

    $("#editButton").on('click', async () => {
        let id = modal.find("#id").val().trim();
        let login = modal.find("#login").val().trim();
        let password = modal.find("#password").val().trim();
        let firstname = modal.find('#firstname').val().trim();
        let lastname = modal.find('#lastname').val().trim();
        let age = modal.find("#age").val().trim();
        let rolesArray = modal.find('#updateroles').val();
        let roles = [];
        for (let i = 0; i < rolesArray.length; i++) {
            roles.push(roleJson.filter(item => item.id == rolesArray[i]))
        }
        roles = [].concat.apply([], roles);

        let data = {
            id: id,
            login: login,
            password: password,
            age: age,
            firstname: firstname,
            lastname: lastname,
            roles: roles
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

async function deleteUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();
    let modalForm = $(`#someDefaultModal`)

    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-danger" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="deleteUser">
                <input type="text" class="form-control" id="id" name="id" value="${user.id}" disabled>
                <label th:for="login" class="col-form-label">E-mail</label>
                <input readonly class="form-control" type="text" id="login" value="${user.login}">
                <label th:for="firstname" class="col-form-label">Name</label>
                <input readonly class="form-control" type="firstname" id="firstname" value="${user.firstname}">
                <label th:for="lastname" class="col-form-label">Last name</label>
                <input readonly class="form-control" type="lastname" id="lastname" value="${user.lastname}">
                <label th:for="password" class="col-form-label">Password</label>
                <input readonly class="form-control" type="password" id="password" value="${user.password}">
                <label th:for="age" class="col-form-label">Age</label>
                <input readonly class="form-control" id="age" type="number" value="${user.age}">
                <label th:for="rolesSelected" class="col-form-label">Roles</label>
                <select class="form-select w-100" name="rolesSelected" id="deleteroles" multiple disabled="true"></select>
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    });

    fetch('/api/roles').then(function (response) {
        if (response.ok) {
            modalForm.find('#deleteroles').empty();
            response.json().then(roleList => {
                roleList.forEach(role => {
                    modalForm.find('#deleteroles')
                        .append($('<option>').val(role.id).text(role.role));
                });
            });
        } else {
            console.error('Network request for roles.json failed with response ' + response.status + ': ' + response.statusText);
        }
    });

    $("#deleteButton").on('click', async () => {
        const response = await userFetchService.deleteUser(id);
        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

