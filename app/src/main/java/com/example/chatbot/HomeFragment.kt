package com.example.chatbot

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class HomeFragment : Fragment() {




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
//                val action= introFragmentDirections.actionIntroFragmentToChatFragment()
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



            }
            true
        }
    }
}