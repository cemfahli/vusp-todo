import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: '',
    warning: '',
    todoList: [
/*  {     id: 1,
      task: 'test',} */
    ],
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    },
    setWarning(state, warning) {
      state.warning = warning;
    },
    clearWarning(state) {
      state.warning = '';
    },
    addAllTasks(state, tasks) {
      state.todoList = tasks;
    },
    addTask(state, task) {
      state.todoList.push(task);
    },
    removeTask(state, index) {
      state.todoList.splice(index, 1);
    },
    signOut(state) {
      state.token = '';
      state.warning = '';
      state.toDoList = [];
    },
  },
  getters: {
    getToken(state) {
      return state.token;
    },
    isAuthorized(state) {
      return (state.token !== '');
    },
    getTasks(state) {
      return state.todoList;
    },
    getWarning(state) {
      return state.warning;
    },
    isWarning(state) {
      return (state.warning !== '');
    },
  },
  actions: {
  },
});
