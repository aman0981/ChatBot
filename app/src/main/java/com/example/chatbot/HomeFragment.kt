package com.example.chatbot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.chatbot.login.LoginPhoneNumber
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {



    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val new_chat = requireView().findViewById<FloatingActionButton>(R.id.new_chat)

        new_chat.setOnClickListener {
            findNavController().navigate(R.id.chatFragment)
        }

        setupNavigationDrawer()
    }


    private fun setupNavigationDrawer() {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = requireActivity().findViewById<NavigationView>(R.id.navigation_view)
        val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
        NavigationUI.setupWithNavController(toolbar, navController, drawerLayout)
        navigationView.setupWithNavController(navController)

        navigationView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()


            when (it.itemId) {
               

                R.id.about -> {
                    navController.navigate(R.id.aboutFragment)
                }

                R.id.share -> {
                   shareApp()
                }

                R.id.chat_history -> {
                    navController.navigate(R.id.topicList)
                }

                R.id.add_chat -> {
                    navController.navigate(R.id.chatFragment)
                }

                R.id.logout -> {
                    signOut()
                }
            }
            true
        }

    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/aman0981/ChatBot")

        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        if (shareIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooserIntent)
        }
    }

    private fun signOut(){
        mAuth.signOut()
        val intent = Intent(requireContext(), LoginPhoneNumber::class.java)
        startActivity(intent)
        requireActivity().finish()  // Optional: Finish the current activity if needed

    }
}